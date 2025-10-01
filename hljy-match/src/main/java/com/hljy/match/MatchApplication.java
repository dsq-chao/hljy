package com.hljy.match;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 推荐匹配服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.hljy.match", "com.hljy.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.hljy.match.mapper")
public class MatchApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MatchApplication.class, args);
    }
}
