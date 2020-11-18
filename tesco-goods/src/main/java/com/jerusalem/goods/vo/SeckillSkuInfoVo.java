package com.jerusalem.goods.vo;

import lombok.Data;

import java.math.BigDecimal;

/****
 * @Author: jerusalem
 * @Description: SeckillSkuInfoVo
 * 秒杀商品的秒杀信息
 * @Date 2020/11/18 17:12
 *****/
@Data
public class SeckillSkuInfoVo {

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

    /***
     * 开始时间
     */
    private Long startTime;

    /***
     * 结束时间
     */
    private Long endTime;

    /***
     * 随机码
     */
    private String randomCode;
}
