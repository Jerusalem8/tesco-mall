package com.jerusalem.seckill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/****
 * @Author: jerusalem
 * @Description: ScheduleConfig
 * 定时任务配置类
 * @Date 2020/11/17 11:05
 *****/
@EnableAsync        //开启异步执行
@EnableScheduling       //开始定时任务
@Configuration
public class ScheduleConfig {

    /***
     * Spring内部集成定时任务框架与Quartz的区别
     * 1.Spring的Cron表达式由6位组成，不允许第7位的 年
     * 2.在 周 的位置，1-7代表周一到周日
     * 3.定时任务不应该阻塞
     *      3.1 可以让业务以异步的方式提交到线程池
     *      3.2 支持定时任务线程池（不好用）
     *      3.3 定时任务异步执行
     *              @EnableAsync        //开启异步任务功能
     *              @Async      给异步执行的方法标注
     *
     * 最终：  异步任务+定时任务 -》 定时任务不阻塞
     */
}
