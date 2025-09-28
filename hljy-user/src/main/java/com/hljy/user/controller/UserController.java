package com.hljy.user.controller;

import com.hljy.common.result.Result;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.entity.User;
import com.hljy.user.service.UserService;
import com.hljy.user.vo.UserLoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private  final UserService userService;

    @PostMapping("/login")
    public Result<UserLoginVo> login(@RequestBody UserLoginDTO loginDTO){
            log.info("用户{}登录", loginDTO.getUsername());
        UserLoginVo userLoginVo = userService.login(loginDTO);
        return Result.success(userLoginVo);
    }

}
