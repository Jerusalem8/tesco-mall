package com.jerusalem.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
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

    @Autowired
    RabbitTemplate rabbitTemplate;

    /****
     * TODO 未知错误：无法自动创建队列、交换机
     * 延时队列实现定时关闭订单
     * 创建一个交换机、两个队列、两个绑定关系
     * @Bean： 注册到容器中，Exchange、Queue、Binding都会自动创建
     * TopicExchange(订阅式交换机) -> name：名字 durable：持久化 exclusive：排他的 autoDelete：自动删除 arguments；自定义属性
     * Queue -> name：名字 durable：持久化 autoDelete：自动删除
     * Binding（绑定关系） -> destination：目的地 DestinationType：目的地类型 exchange：交换机 routingKey：路由键 arguments：自定义参数
     * 注意：队列一旦创建，不会被覆盖
     */

    /***
     * 订单服务交换机
     * @return
     */
    @Bean
    public Exchange orderEventExchange(){
        return new TopicExchange("order-event-exchange",true,false);
    }
    /***
     * 死信队列
     * @return
     */
    @Bean
    public Queue orderDelayQueue(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","order-event-exchange");
        arguments.put("x-dead-letter-routing-key","order.release");
        arguments.put("x-message-ttl",60000);
        return new Queue("order.delay.queue", true, false, false,arguments);
    }
    /***
     * 普通队列
     * @return
     */
    @Bean
    public Queue orderReleaseQueue(){
        return new Queue("order.release.queue", true, false, false);
    }
    @Bean
    public Binding orderCreateBinding(){
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.create", null);
    }
    @Bean
    public Binding orderReleaseBinding(){
        return new Binding("order.release.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.release", null);
    }

    /***
     * 订单释放直接和库存释放进行绑定
     * 防止：订单服务因特殊原因宕机、延迟，库存锁定业务早于订单业务执行完毕，订单状态后期发生改变，导致库存无法解锁
     * @return
     */
    @Bean
    public Binding orderToStockReleaseBinding(){
        return new Binding("stock.release.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.release.other.#", null);
    }


    /***
     * 消息类型转换器
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /***
     * 定制 RabbitTemplate
     * @PostConstruct： MyRabbitMqConfig对象创建完成之后，执行此方法
     */
    @PostConstruct
    public void customRabbitTemplate(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /***
             * 1. 设置消息到达Broker确认回调机制
             * @param correlationData：当前消息的唯一关联数据
             * @param ack：消息是否成功收到
             * @param cause：失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /***
             * 2. 设置消息抵达队列确认回调机制（若投递失败，触发此失败回调）
             * @param message：消息的详细信息
             * @param replyCode：回复的状态码
             * @param replyText：回复的文本内容
             * @param exchange：当时发送消息的交换机
             * @param routingKey：当时发送消息的路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            }
        });
        /***
         * 3. 消费端确认（保证每个消息被正常消费，此时broker才可以删除此消息）
         *      3.1 默认是自动确认（接收消息，客户端自动确认，服务端移除消息）
         *      3.2 问题：部分消息处理成功，发生宕机，消息丢失
         *      3.3 手动确认模式：rabbitmq.listener.simple.acknowledge-mode: manual
         *                      只要没有明确告诉MQ消息被签收，没有ack，货物会一直是unlocked状态；
         *                      即使consumer宕机，消息不会丢失，会重新变为ready状态，下一次有新的consumer连接进来，就发给它。
         *      3.4 如何签收：
         *                  channel.basicAck(deliveryTag,false);签收：业务完成
         *                  channel.basicNack(deliveryTag,false);拒签：业务失败
         */
    }
}
