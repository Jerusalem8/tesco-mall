package com.jerusalem.common.to;

import lombok.Data;

import java.math.BigDecimal;

/****
 * @Author: jerusalem
 * @Description: UserPrice
 * 会员价格信息的封装
 * 用于不同微服务之间的数据传输
 * @Date 2020/4/24 17:55
 *****/
@Data
public class UserPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}