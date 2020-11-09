package com.jerusalem.order.vo;

import com.jerusalem.order.entity.OrdersEntity;
import lombok.Data;

/****
 * @Author: jerusalem
 * @Description: submitOrderResponseVo
 * 下单后的响应结果
 * @Date 2020/11/9 17:33
 *****/
@Data
public class SubmitOrderResponseVo {

    private OrdersEntity order;     //订单
    private Integer code;   //成功/失败 状态码
}
