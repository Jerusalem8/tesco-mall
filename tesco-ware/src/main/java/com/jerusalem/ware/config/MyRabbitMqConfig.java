package com.jerusalem.ware.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/****
 * @Author: jerusalem
 * @Description: MyRabbitConfig
 * 消息中间件RabbitMq相关配置
 * @Date 2020/10/28 16:20
 *****/
@Configuration
public class MyRabbitMqConfig {

    /****
     * Connection refused: connect：错误原因：默认情况下，guest用户被禁止通过远程连接到RabbitMQ
     * TODO 未知错误：无法自动创建队列、交换机 -> 解决：开始监听时，才会自动创建
     * 延时队列实现定时关闭订单
     * 创建一个交换机、两个队列、两个绑定关系
     * @Bean： 注册到容器中，Exchange、Queue、Binding都会自动创建
     * TopicExchange(订阅式交换机) -> name：名字 durable：持久化 exclusive：排他的 autoDelete：自动删除 arguments；自定义属性
     * Queue -> name：名字 durable：持久化 autoDelete：自动删除
     * Binding（绑定关系） -> destination：目的地 DestinationType：目的地类型 exchange：交换机 routingKey：路由键 arguments：自定义参数
     * 注意：队列一旦创建，不会被覆盖
     */

    /***
     * 库存服务交换机
     * @return
     */
    @Bean
    public Exchange stockEventExchange(){
        return new TopicExchange("stock-event-exchange",true,false);
    }
    /***
     * 死信队列
     * @return
     */
    @Bean
    public Queue stockDelayQueue(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","stock-event-exchange");
        arguments.put("x-dead-letter-routing-key","stock.release");
        arguments.put("x-message-ttl",60000);
        return new Queue("stock.delay.queue", true, false, false,arguments);
    }
    /***
     * 普通队列
     * @return
     */
    @Bean
    public Queue stockReleaseQueue(){
        return new Queue("stock.release.queue", true, false, false);
    }
    @Bean
    public Binding stockLockedBinding(){
        return new Binding("stock.delay.queue", Binding.DestinationType.QUEUE, "stock-event-exchange", "stock.locked", null);
    }
    @Bean
    public Binding stockReleaseBinding(){
        return new Binding("stock.release.queue", Binding.DestinationType.QUEUE, "stock-event-exchange", "stock.release.#", null);
    }


    /***
     * 消息类型转换器
     * JSON序列化机制
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
