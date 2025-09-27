package com.hljy.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hljy.common.exception.BusinessException;
import com.hljy.common.result.ResultCode;
import com.hljy.common.utils.JwtUtils;
import com.hljy.common.utils.RedisUtils;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.dto.UserRegisterDTO;
import com.hljy.user.entity.User;
import com.hljy.user.mapper.UserMapper;
import com.hljy.user.service.UserService;
import com.hljy.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    
    @Override
    public UserVO register(UserRegisterDTO registerDTO) {
        // 验证密码一致性
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "两次输入的密码不一致");
        }
        
        // 验证验证码
        if (!verifyCaptcha(registerDTO.getPhone(), registerDTO.getCaptcha(), "register")) {
            throw new BusinessException(ResultCode.CAPTCHA_ERROR);
        }
        
        // 检查用户名是否已存在
        if (getByUsername(registerDTO.getUsername()) != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "用户名已存在");
        }
        
        // 检查手机号是否已存在
        if (getByPhone(registerDTO.getPhone()) != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "手机号已注册");
        }
        
        // 检查邮箱是否已存在
        if (StrUtil.isNotBlank(registerDTO.getEmail()) && getByEmail(registerDTO.getEmail()) != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "邮箱已注册");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt()));
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setNickname(registerDTO.getNickname());
        user.setGender(registerDTO.getGender());
        user.setAge(registerDTO.getAge());
        user.setStatus(0); // 正常状态
        user.setIsVip(0); // 非VIP
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        save(user);
        
        // 返回用户信息
        return BeanUtil.copyProperties(user, UserVO.class);
    }
    
    @Override
    public String login(UserLoginDTO loginDTO) {
        User user = null;
        
        // 根据用户名/手机号/邮箱查找用户
        if (loginDTO.getUsername().matches("^1[3-9]\\d{9}$")) {
            // 手机号登录
            user = getByPhone(loginDTO.getUsername());
        } else if (loginDTO.getUsername().contains("@")) {
            // 邮箱登录
            user = getByEmail(loginDTO.getUsername());
        } else {
            // 用户名登录
            user = getByUsername(loginDTO.getUsername());
        }
        
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 检查用户状态
        if (user.getStatus() != 0) {
            throw new BusinessException(ResultCode.USER_ACCOUNT_DISABLED);
        }
        
        // 验证密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);
        
        // 生成JWT token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        
        // 将token存储到Redis
        redisUtils.set("user:token:" + user.getId(), token, 24 * 60 * 60, java.util.concurrent.TimeUnit.SECONDS);
        
        return token;
    }
    
    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, 0));
    }
    
    @Override
    public User getByPhone(String phone) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone)
                .eq(User::getDeleted, 0));
    }
    
    @Override
    public User getByEmail(String email) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .eq(User::getDeleted, 0));
    }
    
    @Override
    public UserVO getUserById(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return BeanUtil.copyProperties(user, UserVO.class);
    }
    
    @Override
    public UserVO updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
        return BeanUtil.copyProperties(user, UserVO.class);
    }
    
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 验证旧密码
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }
        
        // 更新密码
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }
    
    @Override
    public void resetPassword(String phone, String newPassword, String captcha) {
        // 验证验证码
        if (!verifyCaptcha(phone, captcha, "reset")) {
            throw new BusinessException(ResultCode.CAPTCHA_ERROR);
        }
        
        User user = getByPhone(phone);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 更新密码
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }
    
    @Override
    public void sendCaptcha(String phone, String type) {
        // 生成6位数字验证码
        String captcha = String.format("%06d", new Random().nextInt(1000000));
        
        // 存储验证码到Redis，5分钟过期
        String key = "captcha:" + type + ":" + phone;
        redisUtils.set(key, captcha, 5 * 60, java.util.concurrent.TimeUnit.SECONDS);
        
        // 这里可以集成短信服务发送验证码
        log.info("发送验证码到手机号: {}, 验证码: {}", phone, captcha);
    }
    
    @Override
    public boolean verifyCaptcha(String phone, String captcha, String type) {
        String key = "captcha:" + type + ":" + phone;
        String storedCaptcha = redisUtils.get(key, String.class);
        
        if (StrUtil.isBlank(storedCaptcha)) {
            return false;
        }
        
        boolean isValid = captcha.equals(storedCaptcha);
        if (isValid) {
            // 验证成功后删除验证码
            redisUtils.delete(key);
        }
        
        return isValid;
    }
    
    @Override
    public void logout(String token) {
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId != null) {
            redisUtils.delete("user:token:" + userId);
        }
    }
    
    @Override
    public String refreshToken(String token) {
        Long userId = jwtUtils.getUserIdFromToken(token);
        String username = jwtUtils.getUsernameFromToken(token);
        
        if (userId == null || username == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        
        // 生成新的token
        String newToken = jwtUtils.generateToken(userId, username);
        
        // 更新Redis中的token
        redisUtils.set("user:token:" + userId, newToken, 24 * 60 * 60, java.util.concurrent.TimeUnit.SECONDS);
        
        return newToken;
    }
}
