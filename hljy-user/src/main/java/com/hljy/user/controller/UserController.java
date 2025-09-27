package com.hljy.user.controller;

import com.hljy.common.result.Result;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.dto.UserRegisterDTO;
import com.hljy.user.entity.User;
import com.hljy.user.service.UserService;
import com.hljy.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

}
