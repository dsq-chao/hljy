package com.hljy.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息VO
 */
@Data
public class UserVO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 性别：1-男，2-女
     */
    private Integer gender;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 个人简介
     */
    private String bio;
    
    /**
     * 兴趣标签，JSON字符串
     */
    private String interests;
    
    /**
     * 所在城市
     */
    private String city;
    
    /**
     * 是否VIP：0-否，1-是
     */
    private Integer isVip;
    
    /**
     * VIP过期时间
     */
    private LocalDateTime vipExpireTime;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}