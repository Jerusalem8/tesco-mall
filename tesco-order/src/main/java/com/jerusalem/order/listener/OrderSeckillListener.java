package com.jerusalem.order.listener;

import com.jerusalem.common.to.SeckillOrderTo;
import com.jerusalem.order.entity.OrdersEntity;
import com.jerusalem.order.service.OrdersService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

/****
 * @Author: jerusalem
 * @Description: OrderCloseListener
 * 秒杀订单监听器
 * @Date 2020/11/15 13:44
 *****/
@Component
@RabbitListener(queues = "order.seckill.queue")
public class OrderSeckillListener {

    @Autowired
    OrdersService ordersService;

    @RabbitHandler
    public void handleCloseOrder(SeckillOrderTo seckillOrderTo, Channel channel, Message message) throws IOException {
        System.out.println("准备创建秒杀订单"+seckillOrderTo.getOrderSn()+"的详细信息");
        try {
            ordersService.createSeckillOrder(seckillOrderTo);
            //执行成功，回复mq消息消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            System.out.println("错误："+e.getMessage());
            //只要出现异常，消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
