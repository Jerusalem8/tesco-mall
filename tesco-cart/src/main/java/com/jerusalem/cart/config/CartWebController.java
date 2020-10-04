package com.jerusalem.cart.config;

import com.jerusalem.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
public class CartWebController implements WebMvcConfigurer {

    /***
     * 添加视图映射控制器
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("cartList");
//        registry.addViewController("/cartList.html").setViewName("cartList");
        registry.addViewController("/success.html").setViewName("success");
    }

    /***
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
