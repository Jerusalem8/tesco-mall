package com.jerusalem.order.to;

import com.jerusalem.order.entity.OrderItemEntity;
import com.jerusalem.order.entity.OrdersEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: OrderCreateTo
 * @Date 2020/11/9 18:59
 *****/
@Data
public class OrderCreateTo {

    /**
     * 订单
     */
    private OrdersEntity order;

    /**
     * 订单项
     */
    private List<OrderItemEntity> orderItems;

    /**
     * 应付金额
     */
    private BigDecimal payPrice;

    /**
     * 运费
     */
    private BigDecimal fare;
}
