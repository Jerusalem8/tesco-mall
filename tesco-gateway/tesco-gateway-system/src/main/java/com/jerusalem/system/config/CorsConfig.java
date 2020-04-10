package com.jerusalem.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/****
 * @Author: jerusalem
 * @Description: CorsConfig
 * 解决跨域问题
 * @Date 2020/4/7 16:12
 *****/
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        /**
         * 配置跨域信息
         * addAllowedHeader：请求头
         * addAllowedMethod：请求方法
         * addAllowedOrigin：请求源
         * setAllowCredentials：允许携带cookie跨域
         */
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);
    }
}
