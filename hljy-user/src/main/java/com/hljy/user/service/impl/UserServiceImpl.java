package com.hljy.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hljy.common.exception.BadRequestException;
import com.hljy.common.properties.JwtProperties;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.entity.User;
import com.hljy.user.mapper.UserMapper;
import com.hljy.user.service.UserService;

import com.hljy.user.util.JwtUtil;
import com.hljy.user.vo.UserLoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    private final JwtProperties jwtProperties;

    @Override
    public UserLoginVo login(UserLoginDTO loginDTO) {
        //1.数据校验
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        //查询 用户
        User user = lambdaQuery().eq(User::getUsername, username).eq(User::getPassword, password).one();
        if (user == null){
            throw new BadRequestException("用户名或密码错误");
        }
        //封装返回
            //生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", username);

        String jwt = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(),jwtProperties.getAdminTtl(),claims);

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUsername(user.getUsername());
        userLoginVo.setUserId(user.getId());
        userLoginVo.setToken(jwt);
        return  userLoginVo;
    }
}
