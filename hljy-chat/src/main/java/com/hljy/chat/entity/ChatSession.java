package com.hljy.chat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 聊天会话实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chat_session")
public class ChatSession {
    
    /**
     * 会话ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户1ID
     */
    @TableField("user1_id")
    private Long user1Id;
    
    /**
     * 用户2ID
     */
    @TableField("user2_id")
    private Long user2Id;
    
    /**
     * 最后消息内容
     */
    @TableField("last_message")
    private String lastMessage;
    
    /**
     * 最后消息时间
     */
    @TableField("last_message_time")
    private LocalDateTime lastMessageTime;
    
    /**
     * 会话状态：0-正常，1-已删除
     */
    @TableField("status")
    private Integer status;
    
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
