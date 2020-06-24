package com.jerusalem.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
 *
 * 3、JSR303
 *   1）、给Bean添加校验注解:javax.validation.constraints，并定义自己的message提示
 *   2)、开启校验功能@Valid
 *      效果：校验错误以后会有默认的响应；
 *   3）、给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 *   4）、分组校验（多场景的复杂校验）
 *         1)、	@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 *          给校验注解标注什么情况需要进行校验
 *         2）、@Validated({AddGroup.class})
 *         3)、默认没有指定分组的校验注解@NotBlank，在分组校验情况@Validated({AddGroup.class})下不生效，只会在@Validated生效；
 *
 *   5）、自定义校验
 *      1）、编写一个自定义的校验注解
 *      2）、编写一个自定义的校验器 ConstraintValidator
 *      3）、关联自定义的校验器和自定义的校验注解
 *      @Documented
 *      @Constraint(validatedBy = { ListValueConstraintValidator.class【可以指定多个不同的校验器，适配不同类型的校验】 })
 *      @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
 *      @Retention(RUNTIME)
 *      public @interface ListValue {}
 *
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
@EnableTransactionManagement        //开启事务管理
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.jerusalem.goods.dao"})
@EnableFeignClients(basePackages = {"com.jerusalem.coupon.feign","com.jerusalem.ware.feign","com.jerusalem.search.feign"})
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

}
