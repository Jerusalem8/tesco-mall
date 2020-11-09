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
 * @Date 2020/4/29 17:20
 *****/
@FeignClient(name="goods-service")
@RequestMapping("goods/spu/info")
public interface SpuInfoFeign {

    /***
     * 查询spu的信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    R info(@PathVariable("id") Long id);

    /***
     * 根据skuId查询Spu信息
     * @param skuId
     * @return
     */
    @GetMapping("skuId/{id}")
    R getSpuInfoBySkuId(@PathVariable("skuId") Long skuId);
}
