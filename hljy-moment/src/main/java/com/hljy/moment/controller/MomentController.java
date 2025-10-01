package com.hljy.moment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hljy.common.result.Result;
import com.hljy.moment.dto.MomentCommentDTO;
import com.hljy.moment.dto.MomentPublishDTO;
import com.hljy.moment.entity.Moment;
import com.hljy.moment.service.MomentService;
import com.hljy.moment.vo.MomentCommentVO;
import com.hljy.moment.vo.MomentTopicVO;
import com.hljy.moment.vo.MomentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * 动态控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/moment")
@RequiredArgsConstructor
public class MomentController {

    private final MomentService momentService;
    private final HttpServletRequest request;

    /**
     * 获取动态列表
     */
    @GetMapping("/list")
    public Result<IPage<MomentVO>> getMoments(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long currentUserId = getCurrentUserId();
            log.info("获取动态列表，页码: {}, 大小: {}, 当前用户: {}", page, size, currentUserId);
            
            Page<Moment> pageParam = new Page<>(page, size);
            IPage<MomentVO> result = momentService.getMomentPage(pageParam, currentUserId);
            
            log.info("动态列表查询结果，总数: {}, 当前页数据量: {}", 
                    result.getTotal(), result.getRecords().size());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取动态列表失败", e);
            return Result.failed("获取动态列表失败");
        }
    }

    /**
     * 发布动态
     */
    @PostMapping("/publish")
    public Result<Boolean> publishMoment(@Valid @RequestBody MomentPublishDTO dto) {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return Result.failed("用户未登录");
            }
            
            log.info("收到发布动态请求，用户ID: {}, 内容: {}, 图片数量: {}, 话题: {}", 
                    currentUserId, dto.getContent(), 
                    dto.getImages() != null ? dto.getImages().size() : 0, 
                    dto.getTopics());
            
            boolean result = momentService.publishMoment(dto, currentUserId);
            log.info("发布动态结果: {}", result);
            
            return result ? Result.success("发布成功", true) : Result.failed("发布失败");
        } catch (Exception e) {
            log.error("发布动态失败", e);
            return Result.failed("发布动态失败");
        }
    }

    /**
     * 点赞/取消点赞动态
     */
    @PostMapping("/like/{momentId}")
    public Result<Boolean> toggleLikeMoment(@PathVariable Long momentId) {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return Result.failed("用户未登录");
            }
            
            boolean isLiked = momentService.toggleLikeMoment(momentId, currentUserId);
            String message = isLiked ? "点赞成功" : "取消点赞成功";
            return Result.success(message, isLiked);
        } catch (Exception e) {
            log.error("点赞操作失败", e);
            return Result.failed("操作失败");
        }
    }

    /**
     * 获取动态评论
     */
    @GetMapping("/{momentId}/comments")
    public Result<List<MomentCommentVO>> getMomentComments(@PathVariable Long momentId) {
        try {
            Long currentUserId = getCurrentUserId();
            List<MomentCommentVO> comments = momentService.getMomentComments(momentId, currentUserId);
            return Result.success(comments);
        } catch (Exception e) {
            log.error("获取动态评论失败", e);
            return Result.failed("获取动态评论失败");
        }
    }

    /**
     * 添加评论
     */
    @PostMapping("/comment")
    public Result<Boolean> addComment(@Valid @RequestBody MomentCommentDTO dto) {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return Result.failed("用户未登录");
            }
            
            boolean result = momentService.addComment(dto, currentUserId);
            return result ? Result.success("评论成功", true) : Result.failed("评论失败");
        } catch (Exception e) {
            log.error("添加评论失败", e);
            return Result.failed("添加评论失败");
        }
    }

    /**
     * 删除动态
     */
    @DeleteMapping("/{momentId}")
    public Result<Boolean> deleteMoment(@PathVariable Long momentId) {
        try {
            Long currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                return Result.failed("用户未登录");
            }
            
            boolean result = momentService.deleteMoment(momentId, currentUserId);
            return result ? Result.success("删除成功", true) : Result.failed("删除失败");
        } catch (Exception e) {
            log.error("删除动态失败", e);
            return Result.failed("删除动态失败");
        }
    }

    /**
     * 获取热门话题
     */
    @GetMapping("/topics")
    public Result<List<MomentTopicVO>> getTrendingTopics() {
        try {
            List<MomentTopicVO> topics = momentService.getTrendingTopics();
            return Result.success(topics);
        } catch (Exception e) {
            log.error("获取热门话题失败", e);
            return Result.failed("获取热门话题失败");
        }
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        try {
            // 首先尝试从请求头中获取用户ID（网关传递的）
            String userIdHeader = request.getHeader("X-User-Id");
            if (userIdHeader != null && !userIdHeader.isEmpty()) {
                try {
                    Long userId = Long.valueOf(userIdHeader);
                    log.debug("从请求头获取用户ID: {}", userId);
                    return userId;
                } catch (NumberFormatException e) {
                    log.warn("请求头中的用户ID格式错误: {}", userIdHeader);
                }
            }
            
            // 如果请求头中没有，尝试从Spring Security获取
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.debug("当前认证信息: {}", authentication);
            
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                log.debug("当前用户主体: {}", principal);
                
                // 检查是否是匿名用户
                if (principal instanceof String) {
                    String userIdStr = (String) principal;
                    log.debug("用户ID字符串: {}", userIdStr);
                    
                    // 如果是匿名用户，返回null
                    if ("anonymousUser".equals(userIdStr)) {
                        log.warn("用户未登录，是匿名用户");
                        return null;
                    }
                    
                    try {
                        Long userId = Long.valueOf(userIdStr);
                        log.debug("解析用户ID成功: {}", userId);
                        return userId;
                    } catch (NumberFormatException e) {
                        log.warn("用户ID格式错误: {}, 错误: {}", userIdStr, e.getMessage());
                        return null;
                    }
                } else {
                    log.warn("用户主体类型不是String: {}", principal != null ? principal.getClass() : "null");
                }
            } else {
                log.warn("认证信息为空或未认证: {}", authentication);
            }
        } catch (Exception e) {
            log.error("获取当前用户ID异常", e);
        }
        return null;
    }
}
