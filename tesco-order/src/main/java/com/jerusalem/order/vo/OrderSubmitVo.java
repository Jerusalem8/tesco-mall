package com.jerusalem.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/****
 * @Author: jerusalem
 * @Description: OrderSubmitVo
 * 封装提交订单时需要提交的数据信息
 * @Date 2020/11/8 18:13
 *****/
@Data
public class OrderSubmitVo {

    private Long addrId;    //收货地址Id
    private Integer payType;    //支付方式
    private String orderToken;  //订单防重令牌
    private BigDecimal payPrice;    //应付金额
    private String note;    //订单备注

    //无需提交需要购买的商品，去购物车获取一遍即可
    //用户相关信息，直接去session中取出登录用户
    //TODO 优惠，发票
}
