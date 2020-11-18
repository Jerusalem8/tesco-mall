package com.jerusalem.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;

/****
 * @Author: jerusalem
 * @Description: SeckillSkuVo
 * 秒杀商品的基本秒杀信息
 * @Date 2020/11/17 16:28
 *****/
@Data
public class SeckillSkuVo {

    /**
     * id
     */
    private Long id;
    /**
     * 活动id
     */
    private Long promotionId;
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
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;
}
