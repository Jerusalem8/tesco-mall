package com.jerusalem.common.to;

import lombok.Data;

import java.math.BigDecimal;

/****
 * @Author: jerusalem
 * @Description: QuickOrderTo
 * 秒杀订单的数据封装
 * @Date 2020/11/19 17:22
 *****/
@Data
public class SeckillOrderTo {

    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀数量
     */
    private Integer num;
    /***
     * 用户id
     */
    private Long userId;
}
