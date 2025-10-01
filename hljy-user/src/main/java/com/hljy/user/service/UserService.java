package com.hljy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.dto.UserRegisterDTO;
import com.hljy.user.dto.UserUpdateDTO;
import com.hljy.user.entity.User;
import com.hljy.user.vo.UserLoginVo;
import com.hljy.user.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 用户信息
     */
    UserVO register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    UserLoginVo login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);
    
    /**
     * 根据手机号获取用户信息
     * @param phone 手机号
     * @return 用户信息
     */
    User getUserByPhone(String phone);
    
    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息VO
     */
    UserVO getUserInfo(Long userId);
    
    /**
     * 更新用户信息
     * @param updateDTO 更新信息
     * @return 用户信息VO
     */
    UserVO updateUserInfo(UserUpdateDTO updateDTO);
    
    /**
     * 验证用户密码
     * @param password 明文密码
     * @param encodedPassword 加密密码
     * @return 是否匹配
     */
    boolean validatePassword(String password, String encodedPassword);
    
    /**
     * 加密密码
     * @param password 明文密码
     * @return 加密密码
     */
    String encodePassword(String password);
    
    /**
     * 批量获取用户信息
     * @param userIds 用户ID列表
     * @return 用户信息列表
     */
    List<UserVO> getBatchUserInfo(List<Long> userIds);
    
    /**
     * 获取推荐用户列表
     * @param excludeUserIds 排除的用户ID列表
     * @param gender 性别筛选
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @param city 城市筛选
     * @param limit 限制数量
     * @return 推荐用户列表
     */
    List<UserVO> getRecommendUsers(List<Long> excludeUserIds, Integer gender, 
                                  Integer minAge, Integer maxAge, String city, Integer limit);
}