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
public class UserServiceImpl extends ServiceImpl implements UserService {
    
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

}
