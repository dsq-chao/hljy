package com.hljy.match.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 匹配记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("match_record")
public class Match {
    
    /**
     * 匹配ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 被匹配用户ID
     */
    @TableField("target_user_id")
    private Long targetUserId;
    
    /**
     * 匹配状态：0-待匹配，1-已匹配，2-已拒绝
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 匹配类型：1-推荐，2-搜索
     */
    @TableField("match_type")
    private Integer matchType;
    
    /**
     * 匹配分数
     */
    @TableField("score")
    private Double score;
    
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
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
