package com.jerusalem.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/****
 * @Author: jerusalem
 * @Description: TescoFeignConfig
 * 乐购商城各服务远程调用请求配置
 * 解决远程调用请求头丢失问题
 * @Date 2020/11/7 15:32
 *****/
@Configuration
public class TescoFeignConfig {

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor(){
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //上下文环境的保持器,拿到刚进来的请求
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = requestAttributes.getRequest();//老请求
                //同步请求头信息（cookie等）
                String cookie = request.getHeader("Cookie");
                requestTemplate.header("Cookie",cookie);//新请求同步cookie
            }
        };
    }
}
