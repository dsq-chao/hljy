package com.hljy.match.controller;

import com.hljy.common.result.Result;
import com.hljy.match.service.MatchService;
import com.hljy.match.vo.MatchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 匹配控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {
    
    private final MatchService matchService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取推荐用户列表
     */
    @GetMapping("/recommend")
    public Result<List<MatchVO>> getRecommendUsers(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) String city) {
        try {
            log.info("获取推荐用户列表请求: userId={}, page={}, size={}", userId, page, size);
            
            // 临时使用固定用户ID进行测试
            if (userId == null) {
                userId = 1L;
            }
            
            List<MatchVO> recommendUsers = matchService.getRecommendUsers(userId);
            log.info("获取推荐用户列表成功: userId={}, count={}", userId, recommendUsers.size());
            
            return Result.success("获取成功", recommendUsers);
        } catch (Exception e) {
            log.error("获取推荐用户列表失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 根据用户ID获取推荐用户列表（兼容旧接口）
     */
    @GetMapping("/recommend/{userId}")
    public Result<List<MatchVO>> getRecommendUsersById(@PathVariable Long userId) {
        try {
            log.info("根据用户ID获取推荐用户列表请求: userId={}", userId);
            
            List<MatchVO> recommendUsers = matchService.getRecommendUsers(userId);
            log.info("根据用户ID获取推荐用户列表成功: userId={}, count={}", userId, recommendUsers.size());
            
            return Result.success("获取成功", recommendUsers);
        } catch (Exception e) {
            log.error("根据用户ID获取推荐用户列表失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 喜欢用户
     */
    @PostMapping("/like")
    public Result<String> likeUser(
            @RequestParam Long userId,
            @RequestParam Long targetUserId) {
        try {
            log.info("喜欢用户请求: userId={}, targetUserId={}", userId, targetUserId);
            
            matchService.likeUser(userId, targetUserId);
            log.info("喜欢用户成功: userId={}, targetUserId={}", userId, targetUserId);
            
            return Result.success("操作成功");
        } catch (Exception e) {
            log.error("喜欢用户失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 不喜欢用户
     */
    @PostMapping("/dislike")
    public Result<String> dislikeUser(
            @RequestParam Long userId,
            @RequestParam Long targetUserId) {
        try {
            log.info("不喜欢用户请求: userId={}, targetUserId={}", userId, targetUserId);
            
            matchService.dislikeUser(userId, targetUserId);
            log.info("不喜欢用户成功: userId={}, targetUserId={}", userId, targetUserId);
            
            return Result.success("操作成功");
        } catch (Exception e) {
            log.error("不喜欢用户失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 获取匹配成功列表
     */
    @GetMapping("/matches/{userId}")
    public Result<List<MatchVO>> getMatches(@PathVariable Long userId) {
        try {
            log.info("获取匹配成功列表请求: userId={}", userId);
            
            List<MatchVO> matches = matchService.getMatches(userId);
            log.info("获取匹配成功列表成功: userId={}, count={}", userId, matches.size());
            
            return Result.success("获取成功", matches);
        } catch (Exception e) {
            log.error("获取匹配成功列表失败: {}", e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("匹配服务运行正常");
    }
    
    /**
     * 执行SQL（临时用于修复数据库表结构）
     */
    @PostMapping("/execute-sql")
    public Result<String> executeSql(@RequestBody String sql) {
        try {
            log.info("执行SQL: {}", sql);
            jdbcTemplate.execute(sql);
            log.info("SQL执行成功");
            return Result.success("SQL执行成功");
        } catch (Exception e) {
            log.error("SQL执行失败: {}", e.getMessage());
            return Result.failed("SQL执行失败: " + e.getMessage());
        }
    }
}
