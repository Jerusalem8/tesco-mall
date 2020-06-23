package com.jerusalem.goods.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/****
 * Redisson分布式框架配置类
 * @Author: jerusalem
 * @Description: MyRedissonConfig
 * @Date 2020/6/22 11:49
 *****/
@Configuration
public class MyRedissonConfig {

//    /***
//     * 集群模式
//     * @return
//     * @throws IOException
//     */
//    @Bean(destroyMethod = "shutdown")
//    public RedissonClient redissonClient() throws IOException{
//        Config config = new Config();
//        config.useClusterServers()
//                .addNodeAddress("192.168.75.140:6379","192.168.75.140:6379");
//        return Redisson.create(config);
//    }

    /***
     * 单节点模式
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException{
        //1. 创建配置
        Config config = new Config();
        //地址必须加前缀redis:// ，若要开启SSL，加前缀rediss://
        config.useSingleServer().setAddress("redis://192.168.75.140:6379");
        //2. 创建并返回RedissonClient实例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
