package com.jerusalem.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SkuReductionTo
 * SKU满减、优惠信息的封装
 * 用于不同微服务之间的数据传输
 * @Date 2020/4/24 17:57
 *****/
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<UserPrice> memberPrice;
}
