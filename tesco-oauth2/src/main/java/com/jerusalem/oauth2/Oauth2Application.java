package com.jerusalem.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 1、整合 MyBatis-Plus
 *      1）、导入依赖
 *      2）、配置
 *          1、配置数据源；
 *              1）、导入数据库的驱动
 *              2）、在application.yml配置数据源相关信息
 *          2、配置MyBatis-Plus；
 *              1）、使用@MapperScan
 *              2）、告诉MyBatis-Plus，sql映射文件位置
 *
 * 2、逻辑删除
 *  1）、配置全局的逻辑删除规则（省略）
 *  2）、配置逻辑删除的组件Bean（省略）
 *  3）、给Bean加上逻辑删除注解@TableLogic
 * 4、统一的异常处理
 * @ControllerAdvice
 *  1）、编写异常处理类，使用@ControllerAdvice。
 *  2）、使用@ExceptionHandler标注方法可以处理的异常。
 *
 * 5、模板引擎thymeleaf的使用
 *  1）、引入依赖，关闭缓存
 *  2）、静态资源放在static文件夹下可以根据路径直接访问
 *  3）、页面放在templates文件夹下可以根据路径直接访问（springboot项目访问时，默认查找index）
 *  4）、页面修改后实时更新
 *      （1）、引入dev-tools
 *      （2）、修改完页面，重新编译一下(ctrl+shift+f9)
 * 6、redis缓存
 *  1）、引入依赖
 *  2）、yml配置
 *  3）、使用RedisTemplate
 * 7、Redisson - 分布式锁等功能框架
 *  1）、引入依赖
 *  2）、配置
 * 8、SpringCache - 简化缓存开发
 *  1）、引入依赖
 *  2）、配置
 */
/****
 * @Author:jerusalem
 * @Description: 启动类
 ****/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jerusalem.third.feign","com.jerusalem.user.feign"})
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
