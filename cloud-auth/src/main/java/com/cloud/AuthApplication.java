package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 鉴权中心启动类
 *
 * @author guojianbo
 * @date 2023/6/16 16:42
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cloud.remote"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class);
        System.out.println("鉴权中心==启动成功");
    }
}
