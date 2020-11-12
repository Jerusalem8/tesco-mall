package com.jerusalem.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

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
 *
 *  Seata控制分布式事务
 *      1）、所有微服务必须创建undo_log表
 *      2）、安装事务协调器 https://github.com/seata/seata/releases,下载服务器软件包，将其解压缩（版本问题严重，一定要注意seata-server的版本与seata-all的依赖版本相同）
 *      3）、整合
 *          导入依赖spring-cloud-starter-alibaba-seata
 *          registry.conf：修改注册中心、配置中心
 *          file.conf：配置文件 修改事务日志的存储位置——》更改为数据库存储（建表Sql -》 db_store.sql）
 *          运行nacos-config.sh（命令 ./nacos-config.sh nacosIp）
 *      4)、所有分布式事务使用seata DataSourceProxy代理自己的数据源
 *      5）、所有微服务导入registry.conf、file.conf 并修改#vgroup->rgroup
 *      6）、给分布式事务的主事务标注解@GlobalTransactional  @Transactional   其他分支事务标注解@Transactional
 *
 *
 *
 ****/
@EnableAspectJAutoProxy(exposeProxy = true)
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