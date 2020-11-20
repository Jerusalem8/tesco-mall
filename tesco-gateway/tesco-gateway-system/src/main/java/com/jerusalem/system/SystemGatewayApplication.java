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
//TODO 整合网关流控，定制网关流控回调【http://www.gulixueyuan.com/course/370/task/13616/show】
//TODO 响应式编程

@SpringBootApplication
@EnableDiscoveryClient          // 开启Nacos客户端
public class SystemGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemGatewayApplication.class, args);
    }

}
