package com.jerusalem.common.esTo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SkuEsModel
 * 需要上传到Es的领域模型
 * 用于微服务之间的数据传输
 * @Date 2020/5/17 15:14
 *****/
@Data
public class SkuEsModel {

    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long categoryId;
    private String brandName;
    private String brandImg;
    private String categoryName;
    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
