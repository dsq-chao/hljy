package com.hljy.match.feign;

import com.hljy.common.result.Result;
import com.hljy.user.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "hljy-user", url = "http://localhost:8081", path = "/api/user")
public interface UserServiceClient {
    
    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/info/{userId}")
    Result<UserVO> getUserInfoById(@PathVariable("userId") Long userId);
    
    /**
     * 批量获取用户信息
     */
    @GetMapping("/info/batch")
    Result<List<UserVO>> getBatchUserInfo(@RequestParam("userIds") List<Long> userIds);
    
    /**
     * 获取推荐用户列表（根据条件筛选）
     */
    @GetMapping("/recommend")
    Result<List<UserVO>> getRecommendUsers(
            @RequestParam(value = "excludeUserIds", required = false) List<Long> excludeUserIds,
            @RequestParam(value = "gender", required = false) Integer gender,
            @RequestParam(value = "minAge", required = false) Integer minAge,
            @RequestParam(value = "maxAge", required = false) Integer maxAge,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    );
}
