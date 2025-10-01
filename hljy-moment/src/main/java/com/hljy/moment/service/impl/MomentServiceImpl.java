package com.hljy.moment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hljy.moment.dto.MomentCommentDTO;
import com.hljy.moment.dto.MomentPublishDTO;
import com.hljy.moment.entity.Moment;
import com.hljy.moment.entity.MomentComment;
import com.hljy.moment.entity.MomentLike;
import com.hljy.moment.entity.MomentTopic;
import com.hljy.moment.mapper.MomentCommentMapper;
import com.hljy.moment.mapper.MomentLikeMapper;
import com.hljy.moment.mapper.MomentMapper;
import com.hljy.moment.mapper.MomentTopicMapper;
import com.hljy.moment.service.MomentService;
import com.hljy.moment.vo.MomentCommentVO;
import com.hljy.moment.vo.MomentTopicVO;
import com.hljy.moment.vo.MomentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {

    private final MomentMapper momentMapper;
    private final MomentLikeMapper momentLikeMapper;
    private final MomentCommentMapper momentCommentMapper;
    private final MomentTopicMapper momentTopicMapper;
    private final ObjectMapper objectMapper;

    @Override
    public IPage<MomentVO> getMomentPage(Page<Moment> page, Long currentUserId) {
        // 查询动态分页数据
        IPage<Moment> momentPage = momentMapper.selectMomentPageWithUser(page, currentUserId);
        
        // 转换为VO
        List<MomentVO> momentVOList = momentPage.getRecords().stream()
                .map(this::convertToMomentVO)
                .collect(Collectors.toList());
        
        // 设置点赞状态
        if (currentUserId != null) {
            momentVOList.forEach(momentVO -> {
                boolean isLiked = momentMapper.checkUserLikedMoment(momentVO.getId(), currentUserId) > 0;
                momentVO.setIsLiked(isLiked);
            });
        }
        
        // 构建返回结果
        Page<MomentVO> result = new Page<>(page.getCurrent(), page.getSize(), momentPage.getTotal());
        result.setRecords(momentVOList);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishMoment(MomentPublishDTO dto, Long userId) {
        try {
            log.info("开始发布动态，用户ID: {}, 内容长度: {}", userId, dto.getContent().length());
            
            Moment moment = new Moment();
            moment.setUserId(userId);
            moment.setContent(dto.getContent());
            
            // 处理图片列表
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                log.info("处理图片列表，图片数量: {}", dto.getImages().size());
                moment.setImages(objectMapper.writeValueAsString(dto.getImages()));
            }
            
            // 处理话题标签
            if (dto.getTopics() != null && !dto.getTopics().isEmpty()) {
                log.info("处理话题标签: {}", dto.getTopics());
                moment.setTopics(objectMapper.writeValueAsString(dto.getTopics()));
                // 更新话题使用次数
                updateTopicUsageCount(dto.getTopics());
            }
            
            moment.setLikeCount(0);
            moment.setCommentCount(0);
            moment.setShareCount(0);
            moment.setCreateTime(LocalDateTime.now());
            moment.setUpdateTime(LocalDateTime.now());
            
            boolean result = save(moment);
            log.info("动态保存结果: {}, 动态ID: {}", result, moment.getId());
            
            return result;
        } catch (JsonProcessingException e) {
            log.error("发布动态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleLikeMoment(Long momentId, Long userId) {
        // 检查是否已点赞
        LambdaQueryWrapper<MomentLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MomentLike::getMomentId, momentId)
                   .eq(MomentLike::getUserId, userId);
        
        MomentLike existingLike = momentLikeMapper.selectOne(queryWrapper);
        
        if (existingLike != null) {
            // 取消点赞
            momentLikeMapper.deleteById(existingLike.getId());
            // 更新动态点赞数
            updateMomentLikeCount(momentId, -1);
            return false;
        } else {
            // 添加点赞
            MomentLike momentLike = new MomentLike();
            momentLike.setMomentId(momentId);
            momentLike.setUserId(userId);
            momentLike.setCreateTime(LocalDateTime.now());
            momentLikeMapper.insert(momentLike);
            // 更新动态点赞数
            updateMomentLikeCount(momentId, 1);
            return true;
        }
    }

    @Override
    public List<MomentCommentVO> getMomentComments(Long momentId, Long currentUserId) {
        List<MomentComment> comments = momentCommentMapper.selectCommentTreeByMomentId(momentId);
        return comments.stream()
                .map(comment -> convertToMomentCommentVO(comment, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addComment(MomentCommentDTO dto, Long userId) {
        MomentComment comment = new MomentComment();
        comment.setMomentId(dto.getMomentId());
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentId(dto.getParentId());
        comment.setLikeCount(0);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        
        boolean result = momentCommentMapper.insert(comment) > 0;
        
        if (result) {
            // 更新动态评论数
            updateMomentCommentCount(dto.getMomentId(), 1);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMoment(Long momentId, Long userId) {
        // 检查动态是否属于当前用户
        Moment moment = getById(momentId);
        if (moment == null || !moment.getUserId().equals(userId)) {
            return false;
        }
        
        // 删除动态（逻辑删除）
        return removeById(momentId);
    }

    @Override
    public List<MomentTopicVO> getTrendingTopics() {
        List<MomentTopic> topics = momentTopicMapper.selectHotTopics(10);
        return topics.stream()
                .map(this::convertToMomentTopicVO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为MomentVO
     */
    private MomentVO convertToMomentVO(Moment moment) {
        MomentVO momentVO = new MomentVO();
        BeanUtils.copyProperties(moment, momentVO);
        
        // 处理图片列表
        if (moment.getImages() != null && !moment.getImages().isEmpty()) {
            try {
                List<String> imageList = objectMapper.readValue(moment.getImages(), new TypeReference<List<String>>() {});
                momentVO.setImages(imageList);
            } catch (JsonProcessingException e) {
                log.error("解析图片列表失败", e);
                momentVO.setImages(new ArrayList<>());
            }
        }
        
        // 处理话题标签
        if (moment.getTopics() != null && !moment.getTopics().isEmpty()) {
            try {
                List<String> topicList = objectMapper.readValue(moment.getTopics(), new TypeReference<List<String>>() {});
                momentVO.setTopics(topicList);
            } catch (JsonProcessingException e) {
                log.error("解析话题标签失败", e);
                momentVO.setTopics(new ArrayList<>());
            }
        }
        
        return momentVO;
    }

    /**
     * 转换为MomentCommentVO
     */
    private MomentCommentVO convertToMomentCommentVO(MomentComment comment, Long currentUserId) {
        MomentCommentVO commentVO = new MomentCommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        
        // 设置点赞状态
        if (currentUserId != null) {
            // TODO: 实现评论点赞状态查询
            commentVO.setIsLiked(false);
        }
        
        return commentVO;
    }

    /**
     * 转换为MomentTopicVO
     */
    private MomentTopicVO convertToMomentTopicVO(MomentTopic topic) {
        MomentTopicVO topicVO = new MomentTopicVO();
        BeanUtils.copyProperties(topic, topicVO);
        return topicVO;
    }

    /**
     * 更新动态点赞数
     */
    private void updateMomentLikeCount(Long momentId, int delta) {
        Moment moment = getById(momentId);
        if (moment != null) {
            moment.setLikeCount(Math.max(0, moment.getLikeCount() + delta));
            updateById(moment);
        }
    }

    /**
     * 更新动态评论数
     */
    private void updateMomentCommentCount(Long momentId, int delta) {
        Moment moment = getById(momentId);
        if (moment != null) {
            moment.setCommentCount(moment.getCommentCount() + delta);
            updateById(moment);
        }
    }

    /**
     * 更新话题使用次数
     */
    private void updateTopicUsageCount(List<String> topics) {
        for (String topicName : topics) {
            LambdaQueryWrapper<MomentTopic> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MomentTopic::getName, topicName);
            
            MomentTopic topic = momentTopicMapper.selectOne(queryWrapper);
            if (topic != null) {
                topic.setUsageCount(topic.getUsageCount() + 1);
                topic.setUpdateTime(LocalDateTime.now());
                momentTopicMapper.updateById(topic);
            } else {
                // 创建新话题
                topic = new MomentTopic();
                topic.setName(topicName);
                topic.setUsageCount(1);
                topic.setCreateTime(LocalDateTime.now());
                topic.setUpdateTime(LocalDateTime.now());
                momentTopicMapper.insert(topic);
            }
        }
    }
}
