package com.jerusalem.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/****
 * @Author: jerusalem
 * @Description: TescoWebController
 * 利用SpringMvc viewcontroller：将请求和页面进行映射
 * 无需写只用来进行页面跳转的空方法
 * @Date 2020/9/22 11:18
 *****/
@Configuration
public class TescoWebController implements WebMvcConfigurer {
    /**
     * 添加视图映射控制器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/register.html").setViewName("register");
    }
}
