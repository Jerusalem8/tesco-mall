package com.jerusalem.ware;

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
@MapperScan(basePackages = {"com.jerusalem.ware.dao"})
public class WareApplication {
    public static void main(String[] args) {
        SpringApplication.run(WareApplication.class, args);
    }

}
