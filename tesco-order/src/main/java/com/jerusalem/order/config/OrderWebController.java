package com.jerusalem.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/****
 * @Author: jerusalem
 * @Description: OrderWebController
 * 用SpringMvc viewcontroller：将请求和页面进行映射
 * 无需写只用来进行页面跳转的空方法
 * @Date 2020/10/28 20:33
 *****/
@Configuration
public class OrderWebController implements WebMvcConfigurer {

    /***
     * 添加视图映射控制器
     * @param registry
     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/confirm.html").setViewName("confirm");
//        registry.addViewController("/wait.html").setViewName("wait");
//        registry.addViewController("/orderList.html").setViewName("orderList");
//        registry.addViewController("/pay.html").setViewName("pay");
//    }
}
