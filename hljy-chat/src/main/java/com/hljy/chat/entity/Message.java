package com.hljy.chat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消息实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message")
public class Message {
    
    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 会话ID
     */
    @TableField("session_id")
    private Long sessionId;
    
    /**
     * 发送者ID
     */
    @TableField("sender_id")
    private Long senderId;
    
    /**
     * 接收者ID
     */
    @TableField("receiver_id")
    private Long receiverId;
    
    /**
     * 消息内容
     */
    @TableField("content")
    private String content;
    
    /**
     * 消息类型：1-文本，2-图片，3-表情，4-语音
     */
    @TableField("message_type")
    private Integer messageType;
    
    /**
     * 消息状态：1-已发送，2-已送达，3-已读
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
