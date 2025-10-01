package com.hljy.moment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 动态话题实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("moment_topic")
public class MomentTopic {

    /**
     * 话题ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 话题名称
     */
    @TableField("name")
    private String name;

    /**
     * 话题描述
     */
    @TableField("description")
    private String description;

    /**
     * 使用次数
     */
    @TableField("usage_count")
    private Integer usageCount;

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
}
