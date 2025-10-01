package com.hljy.moment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 动态广场服务启动类
 */
@SpringBootApplication
@MapperScan("com.hljy.moment.mapper")
@EnableFeignClients
public class MomentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomentApplication.class, args);
    }
}
