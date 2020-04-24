package com.jerusalem.common.to;

import lombok.Data;

import java.math.BigDecimal;

/****
 * @Author: jerusalem
 * @Description: SpuBoundsTo
 * SPU积分信息的封装
 * 用于不同微服务之间的数据传输
 * @Date 2020/4/24 17:55
 *****/
@Data
public class SpuBoundsTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
