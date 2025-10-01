package com.hljy.moment.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动态VO
 */
@Data
public class MomentVO {

    /**
     * 动态ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 动态内容
     */
    private String content;

    /**
     * 图片列表
     */
    private List<String> images;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 分享数
     */
    private Integer shareCount;

    /**
     * 话题标签
     */
    private List<String> topics;

    /**
     * 是否已点赞
     */
    private Boolean isLiked;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
