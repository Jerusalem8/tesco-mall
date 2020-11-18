package com.jerusalem.seckill.controller;

import com.jerusalem.common.utils.R;
import com.jerusalem.seckill.service.SeckillSkuService;
import com.jerusalem.seckill.to.SeckillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SeckillController
 * 秒杀
 * @Date 2020/11/18 14:43
 *****/
@RestController
@RequestMapping("seckill")
public class SeckillController {

    @Autowired
    SeckillSkuService seckillSkuService;

    /***
     * 获取当前时间可以参与的秒杀商品信息
     * @return
     */
    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus(){
        List<SeckillSkuRedisTo> seckillSkuRedisTos = seckillSkuService.getCurrentSeckillSkus();
        return R.ok().setData(seckillSkuRedisTos);
    }

    /***
     * 获取sku的秒杀信息
     * 查询是否是秒杀商品
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId")Long skuId){
        SeckillSkuRedisTo seckillSkuRedisTo = seckillSkuService.getSkuSeckillInfo(skuId);
        return R.ok().setData(seckillSkuRedisTo);
    }
}
