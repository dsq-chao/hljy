package com.hljy.user.vo;

import lombok.Data;

/**
 * 用户登录响应VO
 */
@Data
public class UserLoginVo {
    
    /**
     * 用户信息
     */
    private UserVO user;
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * Token过期时间（毫秒）
     */
    private Long expireTime;
}