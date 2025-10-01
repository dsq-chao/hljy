package com.hljy.moment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动态实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("moment")
public class Moment {

    /**
     * 动态ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 动态内容
     */
    @TableField("content")
    private String content;

    /**
     * 图片URL列表（JSON格式）
     */
    @TableField("images")
    private String images;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 评论数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 分享数
     */
    @TableField("share_count")
    private Integer shareCount;

    /**
     * 话题标签（JSON格式）
     */
    @TableField("topics")
    private String topics;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // 非数据库字段
    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private Boolean isLiked;

    @TableField(exist = false)
    private List<String> imageList;

    @TableField(exist = false)
    private List<String> topicList;
}
