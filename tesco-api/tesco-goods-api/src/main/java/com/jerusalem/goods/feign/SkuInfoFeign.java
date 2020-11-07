package com.jerusalem.goods.feign;

import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;


/****
 * @Author: jerusalem
 * @Description: SkuInfoFeign业务层接口
 * @Date 2020/10/14 19:44
 *****/
@FeignClient(name="goods-service")
@RequestMapping("goods/sku/info")
public interface SkuInfoFeign {

    /***
     * 查询
     * @return
     */
    @RequestMapping("/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);

    /****
     * 根据id查询商品的价格
     * @param skuId
     * @return
     */
    @GetMapping("{skuId}/price")
    BigDecimal getPrice(@PathVariable("skuId") Long skuId);
}
