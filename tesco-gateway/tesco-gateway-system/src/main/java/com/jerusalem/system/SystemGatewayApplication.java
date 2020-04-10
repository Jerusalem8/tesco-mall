package com.jerusalem.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/****
 * @Author:jerusalem
 * @Description: 启动类
 ****/

@SpringBootApplication
@EnableDiscoveryClient          // 开启Nacos客户端
public class SystemGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemGatewayApplication.class, args);
    }

}
