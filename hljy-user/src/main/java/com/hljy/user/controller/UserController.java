package com.hljy.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hljy.common.result.Result;
import com.hljy.user.dto.UserLoginDTO;
import com.hljy.user.dto.UserRegisterDTO;
import com.hljy.user.dto.UserUpdateDTO;
import com.hljy.user.entity.User;
import com.hljy.user.service.UserService;
import com.hljy.user.vo.UserLoginVo;
import com.hljy.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<UserLoginVo> login(@Validated @RequestBody UserLoginDTO loginDTO) {
        try {
            log.info("用户登录请求: {}", loginDTO.getUsername());
            UserLoginVo loginVO = userService.login(loginDTO);
            log.info("用户登录成功: {}", loginDTO.getUsername());
            return Result.success("登录成功", loginVO);
        } catch (Exception e) {
            log.error("用户登录失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<UserVO> register(@Validated @RequestBody UserRegisterDTO registerDTO) {
        try {
            log.info("用户注册请求: {}", registerDTO.getUsername());
            UserVO userVO = userService.register(registerDTO);
            log.info("用户注册成功: {}", userVO.getUsername());
            return Result.success("注册成功", userVO);
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        try {
            // 从Security上下文中获取当前用户ID
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Result.failed("用户未认证");
            }
            
            Long userId = Long.valueOf(authentication.getName());
            log.info("获取用户信息请求: userId={}", userId);
            
            UserVO userVO = userService.getUserInfo(userId);
            log.info("获取用户信息成功: userId={}", userId);
            return Result.success("获取成功", userVO);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     *
     * @param updateDTO 更新信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<UserVO> updateUserInfo(@Validated @RequestBody UserUpdateDTO updateDTO) {
        try {
            log.info("更新用户信息请求: userId={}", updateDTO.getId());
            UserVO userVO = userService.updateUserInfo(updateDTO);
            log.info("更新用户信息成功: userId={}", updateDTO.getId());
            return Result.success("更新成功", userVO);
        } catch (Exception e) {
            log.error("更新用户信息失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 根据ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/info/{userId}")
    public Result<UserVO> getUserInfoById(
            @PathVariable Long userId) {
        try {
            log.info("根据ID获取用户信息请求: userId={}", userId);
            UserVO userVO = userService.getUserInfo(userId);
            log.info("根据ID获取用户信息成功: userId={}", userId);
            return Result.success("获取成功", userVO);
        } catch (Exception e) {
            log.error("根据ID获取用户信息失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 批量获取用户信息
     *
     * @param userIds 用户ID列表
     * @return 用户信息列表
     */
    @GetMapping("/info/batch")
    public Result<List<UserVO>> getBatchUserInfo(@RequestParam List<Long> userIds) {
        try {
            log.info("批量获取用户信息请求: userIds={}", userIds);
            
            if (userIds == null || userIds.isEmpty()) {
                return Result.success("获取成功", List.of());
            }
            
            List<UserVO> userVOs = userService.getBatchUserInfo(userIds);
            log.info("批量获取用户信息成功: count={}", userVOs.size());
            
            return Result.success("获取成功", userVOs);
        } catch (Exception e) {
            log.error("批量获取用户信息失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 获取推荐用户列表
     *
     * @param excludeUserIds 排除的用户ID列表
     * @param gender 性别筛选
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @param city 城市筛选
     * @param limit 限制数量
     * @return 推荐用户列表
     */
    @GetMapping("/recommend")
    public Result<List<UserVO>> getRecommendUsers(
            @RequestParam(required = false) List<Long> excludeUserIds,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("获取推荐用户请求: excludeUserIds={}, gender={}, minAge={}, maxAge={}, city={}, limit={}", 
                    excludeUserIds, gender, minAge, maxAge, city, limit);
            
            List<UserVO> recommendUsers = userService.getRecommendUsers(
                    excludeUserIds, gender, minAge, maxAge, city, limit);
            
            log.info("获取推荐用户成功: count={}", recommendUsers.size());
            return Result.success("获取成功", recommendUsers);
        } catch (Exception e) {
            log.error("获取推荐用户失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 健康检查
     *
     * @return 服务运行正常
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("用户服务运行正常");
    }
}