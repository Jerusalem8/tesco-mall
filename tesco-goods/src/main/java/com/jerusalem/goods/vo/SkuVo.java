package com.jerusalem.goods.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/****
 * 视图对象
 * 1.接收页面传递的数据，封装对象
 * 2.将业务处理后的对象，封装成页面所需的数据
 *
 * SkuVo：商品Sku的封装
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-018 14:48:19
 */
@Data
public class SkuVo {

    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Images> images;
    /**
     * 笛卡儿集合
     */
    private List<String> descar;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<UserPrice> userPrice;
}