package com.jerusalem.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/****
 * @Author:jerusalem
 * @Description: 启动类
 ****/
@EnableTransactionManagement        //开启事务管理
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.jerusalem.ware.dao"})
@EnableFeignClients(basePackages = {"com.jerusalem.goods.feign","com.jerusalem.user.feign"})
public class WareApplication {
    public static void main(String[] args) {
        SpringApplication.run(WareApplication.class, args);
    }

}
