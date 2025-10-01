package com.hljy.match.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hljy.common.result.Result;
import com.hljy.match.entity.Match;
import com.hljy.match.feign.UserServiceClient;
import com.hljy.match.mapper.MatchMapper;
import com.hljy.match.service.MatchService;
import com.hljy.match.vo.MatchVO;
import com.hljy.user.vo.UserVO;
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
 * 匹配服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    
    private final MatchMapper matchMapper;
    private final UserServiceClient userServiceClient;
    
    @Override
    public List<MatchVO> getRecommendUsers(Long userId) {
        try {
            // 1. 获取用户已匹配、已拒绝、已喜欢的用户ID列表
            List<Long> matchedUserIds = matchMapper.selectMatchedUserIds(userId);
            List<Long> rejectedUserIds = matchMapper.selectRejectedUserIds(userId);
            List<Long> likedUserIds = matchMapper.selectLikedUserIds(userId);
            
            // 2. 合并需要排除的用户ID
            List<Long> excludeUserIds = new ArrayList<>();
            excludeUserIds.addAll(matchedUserIds);
            excludeUserIds.addAll(rejectedUserIds);
            excludeUserIds.addAll(likedUserIds);
            excludeUserIds.add(userId); // 排除自己
            
            // 3. 调用用户服务获取推荐用户
            Result<List<UserVO>> result = userServiceClient.getRecommendUsers(
                excludeUserIds, null, null, null, null, 20
            );
            
            if (result.getCode() != 200 || result.getData() == null) {
                log.warn("获取推荐用户失败: {}", result.getMessage());
                return getDefaultRecommendUsers();
            }
            
            // 4. 转换为MatchVO并计算匹配分数
            List<MatchVO> recommendUsers = result.getData().stream()
                .map(this::convertToMatchVO)
                .collect(Collectors.toList());
            
            // 5. 按匹配分数排序
            recommendUsers.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
            
            return recommendUsers;
            
        } catch (Exception e) {
            log.error("获取推荐用户异常: {}", e.getMessage(), e);
            return getDefaultRecommendUsers();
        }
    }
    
    /**
     * 获取默认推荐用户（当服务调用失败时使用）
     */
    private List<MatchVO> getDefaultRecommendUsers() {
        List<MatchVO> recommendUsers = new ArrayList<>();
        
        // 模拟数据
        MatchVO user1 = new MatchVO();
        user1.setUserId(2L);
        user1.setUsername("user2");
        user1.setNickname("小美");
        user1.setGender(2);
        user1.setAge(25);
        user1.setAvatar("https://example.com/avatar2.jpg");
        user1.setBio("喜欢旅行和摄影，热爱生活的阳光女孩");
        user1.setCity("北京");
        user1.setScore(85.5);
        user1.setIsVip(1);
        user1.setInterests("[\"旅行\", \"摄影\", \"美食\", \"音乐\"]");
        recommendUsers.add(user1);
        
        MatchVO user2 = new MatchVO();
        user2.setUserId(3L);
        user2.setUsername("user3");
        user2.setNickname("小雨");
        user2.setGender(2);
        user2.setAge(23);
        user2.setAvatar("https://example.com/avatar3.jpg");
        user2.setBio("热爱生活，喜欢美食");
        user2.setCity("上海");
        user2.setScore(78.0);
        user2.setIsVip(0);
        user2.setInterests("[\"美食\", \"音乐\", \"电影\", \"读书\"]");
        recommendUsers.add(user2);
        
        return recommendUsers;
    }
    
    /**
     * 将UserVO转换为MatchVO
     */
    private MatchVO convertToMatchVO(UserVO userVO) {
        MatchVO matchVO = new MatchVO();
        BeanUtils.copyProperties(userVO, matchVO);
        matchVO.setUserId(userVO.getId());
        
        // 计算匹配分数（简单算法）
        double score = calculateMatchScore(userVO);
        matchVO.setScore(score);
        
        return matchVO;
    }
    
    /**
     * 计算匹配分数
     */
    private double calculateMatchScore(UserVO userVO) {
        double score = 50.0; // 基础分数
        
        // VIP用户加分
        if (userVO.getIsVip() != null && userVO.getIsVip() == 1) {
            score += 10.0;
        }
        
        // 有头像加分
        if (userVO.getAvatar() != null && !userVO.getAvatar().isEmpty()) {
            score += 5.0;
        }
        
        // 有个人简介加分
        if (userVO.getBio() != null && !userVO.getBio().isEmpty()) {
            score += 5.0;
        }
        
        // 有兴趣爱好加分
        if (userVO.getInterests() != null && !userVO.getInterests().isEmpty()) {
            score += 10.0;
        }
        
        // 随机因素
        score += Math.random() * 20.0;
        
        return Math.min(score, 100.0);
    }
    
    @Override
    @Transactional
    public void likeUser(Long userId, Long targetUserId) {
        try {
            // 1. 检查是否已经存在匹配记录
            LambdaQueryWrapper<Match> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Match::getUserId, userId)
                       .eq(Match::getTargetUserId, targetUserId)
                       .eq(Match::getDeleted, 0);
            
            Match existingMatch = matchMapper.selectOne(queryWrapper);
            if (existingMatch != null) {
                log.warn("用户{}已经对用户{}进行过操作", userId, targetUserId);
                return;
            }
            
            // 2. 创建喜欢记录
            Match likeRecord = new Match();
            likeRecord.setUserId(userId);
            likeRecord.setTargetUserId(targetUserId);
            likeRecord.setStatus(0); // 0-待匹配
            likeRecord.setMatchType(1); // 1-推荐
            likeRecord.setScore(calculateMatchScore(userId, targetUserId));
            likeRecord.setCreateTime(LocalDateTime.now());
            likeRecord.setUpdateTime(LocalDateTime.now());
            likeRecord.setDeleted(0);
            
            matchMapper.insert(likeRecord);
            log.info("用户{}喜欢用户{}，记录已创建", userId, targetUserId);
            
            // 3. 检查是否互相喜欢
            int mutualLikeCount = matchMapper.checkMutualLike(userId, targetUserId);
            if (mutualLikeCount > 0) {
                // 互相喜欢，更新状态为已匹配
                updateMatchStatus(userId, targetUserId, 1);
                updateMatchStatus(targetUserId, userId, 1);
                log.info("用户{}和用户{}互相喜欢，匹配成功", userId, targetUserId);
                
                // TODO: 创建聊天会话
                // TODO: 发送匹配成功通知
            }
            
        } catch (Exception e) {
            log.error("用户{}喜欢用户{}失败: {}", userId, targetUserId, e.getMessage(), e);
            throw new RuntimeException("喜欢用户操作失败");
        }
    }
    
    /**
     * 更新匹配状态
     */
    private void updateMatchStatus(Long userId, Long targetUserId, Integer status) {
        LambdaQueryWrapper<Match> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Match::getUserId, userId)
                   .eq(Match::getTargetUserId, targetUserId)
                   .eq(Match::getDeleted, 0);
        
        Match match = matchMapper.selectOne(queryWrapper);
        if (match != null) {
            match.setStatus(status);
            match.setUpdateTime(LocalDateTime.now());
            matchMapper.updateById(match);
        }
    }
    
    /**
     * 计算两个用户的匹配分数
     */
    private Double calculateMatchScore(Long userId, Long targetUserId) {
        // 简单实现，实际应该根据用户信息计算
        return 50.0 + Math.random() * 50.0;
    }
    
    @Override
    @Transactional
    public void dislikeUser(Long userId, Long targetUserId) {
        try {
            // 1. 检查是否已经存在匹配记录
            LambdaQueryWrapper<Match> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Match::getUserId, userId)
                       .eq(Match::getTargetUserId, targetUserId)
                       .eq(Match::getDeleted, 0);
            
            Match existingMatch = matchMapper.selectOne(queryWrapper);
            if (existingMatch != null) {
                log.warn("用户{}已经对用户{}进行过操作", userId, targetUserId);
                return;
            }
            
            // 2. 创建拒绝记录
            Match dislikeRecord = new Match();
            dislikeRecord.setUserId(userId);
            dislikeRecord.setTargetUserId(targetUserId);
            dislikeRecord.setStatus(2); // 2-已拒绝
            dislikeRecord.setMatchType(1); // 1-推荐
            dislikeRecord.setScore(0.0); // 拒绝记录分数为0
            dislikeRecord.setCreateTime(LocalDateTime.now());
            dislikeRecord.setUpdateTime(LocalDateTime.now());
            dislikeRecord.setDeleted(0);
            
            matchMapper.insert(dislikeRecord);
            log.info("用户{}拒绝用户{}，记录已创建", userId, targetUserId);
            
        } catch (Exception e) {
            log.error("用户{}拒绝用户{}失败: {}", userId, targetUserId, e.getMessage(), e);
            throw new RuntimeException("拒绝用户操作失败");
        }
    }
    
    @Override
    public List<MatchVO> getMatches(Long userId) {
        try {
            // 1. 查询匹配成功的用户ID列表
            List<Long> matchedUserIds = matchMapper.selectMutualMatchUserIds(userId);
            
            if (matchedUserIds.isEmpty()) {
                return new ArrayList<>();
            }
            
            // 2. 批量获取用户信息
            Result<List<UserVO>> result = userServiceClient.getBatchUserInfo(matchedUserIds);
            
            if (result.getCode() != 200 || result.getData() == null) {
                log.warn("获取匹配用户信息失败: {}", result.getMessage());
                return new ArrayList<>();
            }
            
            // 3. 转换为MatchVO
            List<MatchVO> matches = result.getData().stream()
                .map(this::convertToMatchVO)
                .collect(Collectors.toList());
            
            // 4. 按匹配时间排序（最新的在前）
            matches.sort((a, b) -> {
                // 这里可以根据实际的匹配时间排序
                return Double.compare(b.getScore(), a.getScore());
            });
            
            return matches;
            
        } catch (Exception e) {
            log.error("获取匹配列表异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
