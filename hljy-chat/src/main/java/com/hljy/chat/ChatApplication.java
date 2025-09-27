package com.hljy.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 聊天服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.hljy.chat", "com.hljy.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class ChatApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
