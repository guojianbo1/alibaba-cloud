package com.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统服务启动类
 *
 * @author guojianbo
 * @date 2023/6/16 17:01
 */
@SpringBootApplication
@EnableFeignClients( basePackages = {"com.cloud.**"})
@EnableDiscoveryClient
@MapperScan("com.cloud.mapper")
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class);
        System.out.println("系统服务===启动成功");
    }
}
