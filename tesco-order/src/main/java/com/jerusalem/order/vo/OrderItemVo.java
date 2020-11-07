package com.jerusalem.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: OrderItemVo
 * 订单项数据模型封装
 * @Date 2020/11/7 13:18
 *****/
@Data
public class OrderItemVo {

    /**
     * 商品ID
     * 标题
     * 图片
     * 属性组合
     * 价格
     * 数量
     * 总价
     */
    private Long skuId;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
}
