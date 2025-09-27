package com.hljy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.dto.UserRegisterDTO;
import com.hljy.user.entity.User;
import com.hljy.user.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     */
    UserVO register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     */
    String login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户名获取用户信息
     */
    User getByUsername(String username);
    
    /**
     * 根据手机号获取用户信息
     */
    User getByPhone(String phone);
    
    /**
     * 根据邮箱获取用户信息
     */
    User getByEmail(String email);
    
    /**
     * 根据用户ID获取用户信息
     */
    UserVO getUserById(Long userId);
    
    /**
     * 更新用户信息
     */
    UserVO updateUser(User user);
    
    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 重置密码
     */
    void resetPassword(String phone, String newPassword, String captcha);
    
    /**
     * 发送验证码
     */
    void sendCaptcha(String phone, String type);
    
    /**
     * 验证验证码
     */
    boolean verifyCaptcha(String phone, String captcha, String type);
    
    /**
     * 用户登出
     */
    void logout(String token);
    
    /**
     * 刷新Token
     */
    String refreshToken(String token);
}
