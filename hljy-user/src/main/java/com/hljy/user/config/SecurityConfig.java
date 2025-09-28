package com.hljy.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF（因为是前后端分离，使用 JWT）
                .csrf().disable()
                // 关闭默认表单登录
                .formLogin().disable()
                // 关闭 HTTP Basic
                .httpBasic().disable()
                // 放行登录、注册接口
                .authorizeRequests()
                .antMatchers("/api/user/login", "/api/user/register").permitAll()
                // 其他接口都需要认证
                .anyRequest().authenticated();
        return http.build();
    }
}

