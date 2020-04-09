package com.jerusalem.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/****
 * @Author:jerusalem
 * @Description: 启动类
 ****/
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.jerusalem.user.dao"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
