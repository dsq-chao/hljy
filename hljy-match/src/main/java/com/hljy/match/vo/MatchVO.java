package com.hljy.match.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 匹配信息VO
 */
@Data
public class MatchVO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 性别
     */
    private Integer gender;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 个人简介
     */
    private String bio;
    
    /**
     * 所在城市
     */
    private String city;
    
    /**
     * 匹配分数
     */
    private Double score;
    
    /**
     * 匹配时间
     */
    private LocalDateTime matchTime;
}
