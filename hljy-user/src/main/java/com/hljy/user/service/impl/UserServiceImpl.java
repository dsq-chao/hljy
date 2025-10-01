package com.hljy.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.dto.UserRegisterDTO;
import com.hljy.user.dto.UserUpdateDTO;
import com.hljy.user.entity.User;
import com.hljy.user.mapper.UserMapper;
import com.hljy.user.service.UserService;
import com.hljy.user.util.JwtUtil;
import com.hljy.user.vo.UserLoginVo;
import com.hljy.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    @Override
    public UserLoginVo login(UserLoginDTO loginDTO) {
        // 1. 根据用户名或手机号查找用户
        User user = getUserByUsernameOrPhone(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 2. 验证密码
        if (!validatePassword(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 3. 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 4. 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);
        
        // 5. 生成JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.createJWT(jwtSecret, jwtExpiration * 1000, claims);
        long expireTime = System.currentTimeMillis() + jwtExpiration * 1000;
        
        // 6. 构建登录响应
        UserLoginVo loginVO = new UserLoginVo();
        loginVO.setUser(convertToUserVO(user));
        loginVO.setToken(token);
        loginVO.setExpireTime(expireTime);
        
        return loginVO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(UserRegisterDTO registerDTO) {
        // 1. 验证密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }
        
        // 2. 检查用户名是否已存在
        if (getUserByUsername(registerDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 3. 检查手机号是否已存在
        if (getUserByPhone(registerDTO.getPhone()) != null) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 4. 检查邮箱是否已存在（如果提供了邮箱）
        if (StrUtil.isNotBlank(registerDTO.getEmail())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, registerDTO.getEmail());
            if (count(wrapper) > 0) {
                throw new RuntimeException("邮箱已存在");
            }
        }
        
        // 5. 创建用户对象
        User user = new User();
        BeanUtil.copyProperties(registerDTO, user);
        user.setPassword(encodePassword(registerDTO.getPassword()));
        user.setStatus(1); // 正常状态
        user.setIsVip(0); // 非VIP
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        // 6. 保存用户
        save(user);
        
        // 7. 返回用户信息
        return convertToUserVO(user);
    }
    
    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }
    
    @Override
    public User getUserByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return getOne(wrapper);
    }
    
    @Override
    public UserVO getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToUserVO(user);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUserInfo(UserUpdateDTO updateDTO) {
        // 1. 检查用户是否存在
        User user = getById(updateDTO.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 2. 更新用户信息
        BeanUtil.copyProperties(updateDTO, user);
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
        
        // 3. 返回更新后的用户信息
        return convertToUserVO(user);
    }
    
    @Override
    public boolean validatePassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
    
    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    /**
     * 根据用户名或手机号获取用户
     */
    private User getUserByUsernameOrPhone(String username) {
        // 先按用户名查找
        User user = getUserByUsername(username);
        if (user != null) {
            return user;
        }
        
        // 如果按用户名没找到，再按手机号查找
        return getUserByPhone(username);
    }
    
    @Override
    public List<UserVO> getBatchUserInfo(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getId, userIds)
               .eq(User::getStatus, 1); // 只查询正常状态的用户
        
        List<User> users = list(wrapper);
        return users.stream()
                .map(this::convertToUserVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserVO> getRecommendUsers(List<Long> excludeUserIds, Integer gender, 
                                         Integer minAge, Integer maxAge, String city, Integer limit) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        // 排除指定的用户ID
        if (excludeUserIds != null && !excludeUserIds.isEmpty()) {
            wrapper.notIn(User::getId, excludeUserIds);
        }
        
        // 只查询正常状态的用户
        wrapper.eq(User::getStatus, 1);
        
        // 性别筛选
        if (gender != null) {
            wrapper.eq(User::getGender, gender);
        }
        
        // 年龄筛选
        if (minAge != null) {
            wrapper.ge(User::getAge, minAge);
        }
        if (maxAge != null) {
            wrapper.le(User::getAge, maxAge);
        }
        
        // 城市筛选
        if (StrUtil.isNotBlank(city)) {
            wrapper.eq(User::getCity, city);
        }
        
        // 限制数量
        wrapper.last("LIMIT " + limit);
        
        // 按创建时间倒序排列
        wrapper.orderByDesc(User::getCreateTime);
        
        List<User> users = list(wrapper);
        return users.stream()
                .map(this::convertToUserVO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将User实体转换为UserVO
     */
    private UserVO convertToUserVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }
}