package com.jerusalem.third;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/****
 * @Author: jerusalem
 * @Description: OssApplication
 * @Date 2020/4/13 13:44
 *****/
@SpringBootApplication
@EnableDiscoveryClient
public class ThirdApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdApplication.class, args);
    }
}
