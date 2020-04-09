package com.jerusalem.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/****
 * @Author:jerusalem
 * @Description: 启动类
 ****/
@SpringBootApplication
@EnableDiscoveryClient          // 开启Nacos客户端
@MapperScan(basePackages = {"com.jerusalem.goods.dao"})
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

}
