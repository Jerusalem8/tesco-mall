package com.jerusalem.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/****
 * @Author: jerusalem
 * @Description: SessionConfig
 * Session配置（cookie的作用域，json的序列化将数据保存到Redis）
 * @Date 2020/9/27 17:10
 *****/
@Configuration
@EnableRedisHttpSession
public class SessionConfig {

    /***
     * 自定义cookie的名字，作用域
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("TESCO-SESSION");
        serializer.setDomainName("tesco.com");
        return serializer;
    }

    /***
     * redis的json序列化机制
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
