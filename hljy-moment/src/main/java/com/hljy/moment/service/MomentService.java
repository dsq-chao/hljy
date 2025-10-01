package com.hljy.moment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hljy.moment.dto.MomentCommentDTO;
import com.hljy.moment.dto.MomentPublishDTO;
import com.hljy.moment.entity.Moment;
import com.hljy.moment.vo.MomentCommentVO;
import com.hljy.moment.vo.MomentTopicVO;
import com.hljy.moment.vo.MomentVO;

import java.util.List;

/**
 * 动态服务接口
 */
public interface MomentService extends IService<Moment> {

    /**
     * 分页查询动态列表
     */
    IPage<MomentVO> getMomentPage(Page<Moment> page, Long currentUserId);

    /**
     * 发布动态
     */
    boolean publishMoment(MomentPublishDTO dto, Long userId);

    /**
     * 点赞/取消点赞动态
     */
    boolean toggleLikeMoment(Long momentId, Long userId);

    /**
     * 获取动态评论列表
     */
    List<MomentCommentVO> getMomentComments(Long momentId, Long currentUserId);

    /**
     * 添加评论
     */
    boolean addComment(MomentCommentDTO dto, Long userId);

    /**
     * 删除动态
     */
    boolean deleteMoment(Long momentId, Long userId);

    /**
     * 获取热门话题
     */
    List<MomentTopicVO> getTrendingTopics();
}
