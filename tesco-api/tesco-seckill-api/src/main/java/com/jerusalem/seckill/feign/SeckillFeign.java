package com.jerusalem.seckill.feign;

import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author: jerusalem
 * @Description: SeckillFeign业务层接口
 * @Date 2020/11/18 17:09
 *****/
@FeignClient(name="seckill-service")
@RequestMapping("seckill")
public interface SeckillFeign {

    /***
     * 获取sku的秒杀信息
     * 查询是否是秒杀商品
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId")Long skuId);
}
