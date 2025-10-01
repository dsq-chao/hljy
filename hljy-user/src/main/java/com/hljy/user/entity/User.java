package com.hljy.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User {
    
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    
    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
    
    /**
     * 性别：1-男，2-女
     */
    @TableField("gender")
    private Integer gender;
    
    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;
    
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    
    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;
    
    /**
     * 个人简介
     */
    @TableField("bio")
    private String bio;
    
    /**
     * 兴趣标签，JSON字符串
     */
    @TableField("interests")
    private String interests;
    
    /**
     * 所在城市
     */
    @TableField("city")
    private String city;
    
    /**
     * 是否VIP：0-否，1-是
     */
    @TableField("is_vip")
    private Integer isVip;
    
    /**
     * VIP过期时间
     */
    @TableField("vip_expire_time")
    private LocalDateTime vipExpireTime;
    
    /**
     * 状态：0-禁用，1-正常
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    
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
     * 是否删除：0-否，1-是
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}