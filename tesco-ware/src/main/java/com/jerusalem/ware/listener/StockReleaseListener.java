package com.jerusalem.ware.listener;

import com.jerusalem.common.to.OrderTo;
import com.jerusalem.common.to.StockLockedTo;
import com.jerusalem.ware.service.WareSkuService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/****
 * @Author: jerusalem
 * @Description: StockReleaseListener
 * 库存解锁监听器
 * @Date 2020/11/14 17:59
 *****/
@Service
@RabbitListener(queues = "stock.release.queue")
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;

    /***
     * 监听队列、处理库存解锁业务
     * 库存解锁的场景：
     *      1.库存锁定成功，下单成功，但是订单过期未支付或被取消 -》 库存解锁
     *      2.库存锁定成功，订单后续业务逻辑失败，下单失败，订单回滚 -》 库存解锁
     *      3.库存锁定失败，库存回滚（此情况无库存工作单详情）-》无需解锁
     * 逻辑分析：
     *      1.查询关于该订单的库存工作单信息是否存在
     *          1.1 有 -》 证明库存锁定成功，查询该订单是否存在
     *              1.1.1  无 -》 证明下单失败，必须解锁
     *              1.1.2  有 -》 证明下单成功，查询订单状态
     *                      1.1.2.1  已取消 -》 解锁库存
     *                      1.1.2.2  未取消（其他任何状态） -》 无需解锁
     *          1.2 无 -》 证明库存锁定失败，所有都回滚，无需解锁
     *
     * @param stockLockedTo
     * @param message
     * @param channel
     */
    @RabbitHandler
    public void handleStockRelease(StockLockedTo stockLockedTo, Message message, Channel channel) throws IOException {
        System.out.println("收到解锁库存的消息");
        try {
            wareSkuService.unLockStock(stockLockedTo);
            //执行成功，回复mq消息消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            System.out.println("错误："+e.getMessage());
            //只要出现异常，消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    /***
     * 监听关闭订单的消息
     * 接受订单关闭消息，解锁库存（防止网络原因导致的库存无法解锁）
     * @param orderTo
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void handleCloseOrderRelease(OrderTo orderTo, Message message, Channel channel) throws IOException {
        System.out.println("收到订单关闭的消息，准备解锁库存");
        try {
            wareSkuService.unLockStock(orderTo);
            //执行成功，回复mq消息消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            System.out.println("错误："+e.getMessage());
            //只要出现异常，消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
