package com.jerusalem.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/****
 * @Author:jerusalem
 * @Description: 启动类 SeckillApplication
 * 秒杀微服务
 * //TODO 上架秒杀商品时设置过期时间
 * //TODO 秒杀后续的流程，简化收货地址等信息
 ****/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jerusalem.coupon.feign","com.jerusalem.goods.feign"})
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
}