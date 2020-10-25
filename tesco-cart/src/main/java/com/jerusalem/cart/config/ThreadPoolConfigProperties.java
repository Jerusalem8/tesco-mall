package com.jerusalem.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/****
 * @Author: jerusalem
 * @Description: ThreadPoolConfigProperties
 * 线程池配置类
 * @Date 2020/10/14 20:20
 *****/
@Data
@Component
@ConfigurationProperties(prefix = "tesco.thread")
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
