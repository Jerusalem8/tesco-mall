package com.jerusalem.order.config;

import com.jerusalem.order.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/****
 * @Author: jerusalem
 * @Description: OrderWebConfiguration
 * 注册拦截器
 * @Date 2020/11/7 12:31
 *****/
@Configuration
public class OrderWebConfiguration implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
