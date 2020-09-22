package com.jerusalem.goods.vo;

import com.jerusalem.goods.entity.SkuImagesEntity;
import com.jerusalem.goods.entity.SkuInfoEntity;
import com.jerusalem.goods.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: skuItem
 * 商品详情页-返回信息封装
 * @Date 2020/9/18 16:59
 *****/
@Data
public class SkuItemVo {

    //1.sku的基本信息（标题，副标题，价格）
    SkuInfoEntity skuInfo;

    //2.sku的图片信息
    List<SkuImagesEntity> skuImages;

    //3.sku的销售属性
    List<SkuSaleAttrVo> skuSaleAttrVos;

    //4.sku的介绍
    SpuInfoDescEntity spuInfoDesc;

    //5.sku的规格参数
    private List<SpuBaseAttrGroupVo> spuBaseAttrGroupVos;

    //6.是否有货
    boolean hasStock = true;
}
