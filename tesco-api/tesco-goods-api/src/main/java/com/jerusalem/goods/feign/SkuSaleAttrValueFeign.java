package com.jerusalem.goods.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SkuSaleAttrValueFeign业务层接口
 * @Date 2020/10/14 21:00
 *****/
@FeignClient(name="goods-service")
@RequestMapping("goods/sku/sale/attr/value")
public interface SkuSaleAttrValueFeign {

    /***
     * 获取Sku商品销售属性组合
     * @param skuId
     * @return
     */
    @GetMapping("/stringlist/{skuId}")
    List<String> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);
}
