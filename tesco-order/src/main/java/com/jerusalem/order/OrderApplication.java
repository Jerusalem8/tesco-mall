package com.jerusalem.order;

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
@MapperScan(basePackages = {"com.jerusalem.order.dao"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
