package com.hljy.user.filter;

import com.hljy.user.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 获取请求头中的Authorization
        String authHeader = request.getHeader("Authorization");
        
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            try {
                // 提取JWT token
                String token = authHeader.substring(7);
                
                // 解析JWT token
                Claims claims = JwtUtil.parseJWT(jwtSecret, token);
                
                // 获取用户ID
                Long userId = Long.valueOf(claims.get("userId").toString());
                
                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userId, 
                        null, 
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                
                // 设置到Security上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("JWT认证成功，用户ID: {}", userId);
                
            } catch (Exception e) {
                log.warn("JWT认证失败: {}", e.getMessage());
                // 认证失败，继续执行，让Spring Security处理
            }
        }
        
        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
}
