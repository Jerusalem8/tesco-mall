package com.jerusalem.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/****
 * @Author:jerusalem
 * @Description: 启动类
 * 1. 引入amqp场景：RabbitAutoConfiguration生效
 * 2. 容器自动配置：CachingConnectionFactory（连接工厂）、RabbitTemplate、AmqpAdmin、RabbitMessagingTemplate
 * 3. 配置文件相关配置
 * 4. @EnableRabbit：开启功能
 * 5. 监听消息：@RabbitListener() --- 标在 类+方法 上（监听哪些队列即可）
 *             @RabbitHandle() --- 标在方法上（重载区分不同的消息）
 *
 *  本地事务失效问题：
 *      同一个对象内事务方法互调默认失效，原因：绕过了代理对象，事务使用代理对象来控制的
 *      解决方法：使用代理对象调用事务方法
 *      1.引入aop-starter 引入了aspectj
 *      2.开启@EnableAspectJAutoRroxy ->开启aspectj动态代理功能 -》 即使没有接口，也可以创建动态代理
 *      3.本类互调用调用对象
 ****/
@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
@MapperScan(basePackages = {"com.jerusalem.order.dao"})
@EnableFeignClients(basePackages = {"com.jerusalem.cart.feign","com.jerusalem.user.feign","com.jerusalem.ware.feign","com.jerusalem.goods.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}