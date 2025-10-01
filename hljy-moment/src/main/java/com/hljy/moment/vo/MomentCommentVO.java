package com.hljy.moment.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动态评论VO
 */
@Data
public class MomentCommentVO {

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 动态ID
     */
    private Long momentId;

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
     * 评论内容
     */
    private String content;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否已点赞
     */
    private Boolean isLiked;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 回复列表
     */
    private List<MomentCommentVO> replies;
}
