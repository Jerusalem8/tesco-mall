package com.jerusalem.goods.vo;

import lombok.Data;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SkuSaleAttrVo
 * @Date 2020/9/20 16:15
 *****/
@Data
public class SkuSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}
