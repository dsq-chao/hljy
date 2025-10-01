package com.hljy.user.config;

import com.hljy.user.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF（因为是前后端分离，使用 JWT）
                .csrf().disable()
                // 关闭默认表单登录
                .formLogin().disable()
                // 关闭 HTTP Basic
                .httpBasic().disable()
                // 设置会话管理为无状态
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 配置授权规则 - 使用旧版本的配置方式
                .authorizeRequests()
                // 放行登录、注册接口
                .antMatchers("/api/user/login").permitAll()
                .antMatchers("/api/user/register").permitAll()
                // 放行健康检查接口
                .antMatchers("/api/user/health").permitAll()
                // 放行错误页面
                .antMatchers("/error").permitAll()
                // 其他接口都需要认证
                .anyRequest().authenticated()
                .and()
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置异常处理
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":403,\"message\":\"访问被拒绝，请先登录\"}");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":401,\"message\":\"未认证，请先登录\"}");
                        })
                );
        return http.build();
    }
}

