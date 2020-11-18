package com.jerusalem.seckill.to;

import com.jerusalem.seckill.vo.SkuDetailInfoVo;
import lombok.Data;

import java.math.BigDecimal;

/****
 * @Author: jerusalem
 * @Description: SeckillSkuRedisTo
 * 秒杀商品的信息缓存到redis
 * @Date 2020/11/17 17:03
 *****/
@Data
public class SeckillSkuRedisTo {

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
     * sku的详细信息
     */
    private SkuDetailInfoVo skuInfo;

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
