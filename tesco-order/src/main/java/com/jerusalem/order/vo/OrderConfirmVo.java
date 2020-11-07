package com.jerusalem.order.vo;

import com.jerusalem.common.vo.OrderItemVo;
import com.jerusalem.common.vo.UserAddressVo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: OrderConfirmVo
 * 订单结算页数据模型封装
 * @Date 2020/11/7 13:06
 *****/
public class OrderConfirmVo {

    /***
     * 用户收货地址
     */
    @Getter @Setter
    List<UserAddressVo> UserAddressVos;

    /***
     * 订单项
     */
    @Getter @Setter
    List<OrderItemVo> orderItemVos;

    /***
     * 优惠信息
     * （暂时只整合用户积分）
     */
    @Getter @Setter
    Integer integration;

    /***
     * 订单总额
     */
    public BigDecimal getTotalPrice(){
        BigDecimal totalPrice = new BigDecimal("0");
        if (orderItemVos != null){
            for (OrderItemVo orderItemVo : orderItemVos) {
                BigDecimal price = orderItemVo.getPrice().multiply(new BigDecimal(orderItemVo.getCount().toString()));
                totalPrice = totalPrice.add(price);
            }
        }
        return totalPrice;
    }

    /***
     * 应付金额
     */
    public BigDecimal getPayPrice() {
        //暂无优惠
        return getTotalPrice();
    }

    /***
     * 订单唯一令牌
     * 防止订单重复提交
     */
    @Getter @Setter
    String orderToken;


    /***
     * 发票（暂不支持）
     */
}
