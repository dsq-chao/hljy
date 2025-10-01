package com.hljy.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 全局认证过滤器
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    // 不需要认证的路径
    private static final List<String> SKIP_AUTH_URLS = Arrays.asList(
            "/api/user/login",
            "/api/user/register",
            "/api/user/send-captcha",
            "/api/user/reset-password",
            "/api/payment/vip-plans",
            "/api/payment/callback",
            "/health",
            "/actuator"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 检查是否为不需要认证的路径
        if (isSkipAuth(path)) {
            return chain.filter(exchange);
        }

        // 获取Authorization头
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "未提供有效的认证信息");
        }

        // 提取token
        String token = authHeader.substring(7);
        if (!StringUtils.hasText(token)) {
            return unauthorized(exchange, "Token不能为空");
        }

        // 简单的token验证（实际项目中应该调用用户服务验证token）
        if (token.length() < 10) {
            return unauthorized(exchange, "Token格式不正确");
        }

        // 从token中解析用户ID（简化版本，实际应该调用JWT工具类）
        String userId = extractUserIdFromToken(token);
        
        // 将用户信息添加到请求头中，传递给下游服务
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", userId != null ? userId : "1") // 从token解析用户ID，如果解析失败则使用默认值
                .header("X-User-Name", "user" + (userId != null ? userId : "1")) // 生成用户名
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    /**
     * 检查是否为跳过认证的路径
     */
    private boolean isSkipAuth(String path) {
        return SKIP_AUTH_URLS.stream().anyMatch(path::startsWith);
    }

    /**
     * 从token中提取用户ID（简化版本）
     */
    private String extractUserIdFromToken(String token) {
        try {
            // 简化版本：假设token包含用户ID信息
            // 实际项目中应该使用JWT工具类解析token
            if (token.contains("user")) {
                // 如果token包含user信息，尝试提取ID
                String[] parts = token.split("\\.");
                if (parts.length >= 2) {
                    // 这里应该解码JWT payload，但为了简化，我们使用固定逻辑
                    // 实际应该解析JWT的payload部分获取用户ID
                    return "1"; // 临时返回固定用户ID
                }
            }
            return "1"; // 默认返回用户ID 1
        } catch (Exception e) {
            return "1"; // 解析失败时返回默认用户ID
        }
    }

    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        String body = String.format("{\"code\":401,\"message\":\"%s\",\"data\":null}", message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100; // 设置优先级，数值越小优先级越高
    }
}

