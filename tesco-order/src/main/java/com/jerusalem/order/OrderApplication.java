package com.jerusalem.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/****
 * @Author:jerusalem
 * @Description: 启动类
 * 1. 引入amqp场景：RabbitAutoConfiguration生效
 * 2. 容器自动配置：CachingConnectionFactory（连接工厂）、RabbitTemplate、AmqpAdmin、RabbitMessagingTemplate
 * 3. 配置文件相关配置
 * 4. @EnableRabbit：开启功能
 * 5. 监听消息：@RabbitListener() --- 标在 类+方法 上（监听哪些队列即可）
 *             @RabbitHandle() --- 标在方法上（重载区分不同的消息）
 ****/
@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
@MapperScan(basePackages = {"com.jerusalem.order.dao"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}