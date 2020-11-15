package com.jerusalem.order.listener;

import com.jerusalem.order.entity.OrdersEntity;
import com.jerusalem.order.service.OrdersService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/****
 * @Author: jerusalem
 * @Description: OrderCloseListener
 * 定时关闭订单监听器
 * @Date 2020/11/15 13:44
 *****/
@Service
@RabbitListener(queues = "order.release.queue")
public class OrderCloseListener {

    @Autowired
    OrdersService ordersService;

    @RabbitHandler
    public void handleCloseOrder(OrdersEntity ordersEntity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单信息，准备关闭订单"+ordersEntity.getOrderSn());
        try {
            ordersService.closeOrder(ordersEntity);
            //执行成功，回复mq消息消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            System.out.println("错误："+e.getMessage());
            //只要出现异常，消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
