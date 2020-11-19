package com.jerusalem.seckill.controller;

import com.jerusalem.common.utils.R;
import com.jerusalem.seckill.service.SeckillSkuService;
import com.jerusalem.seckill.to.SeckillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SeckillController
 * 秒杀
 * @Date 2020/11/18 14:43
 *****/
@Controller
@RequestMapping("seckill")
public class SeckillController {

    @Autowired
    SeckillSkuService seckillSkuService;

    /***
     * 获取当前时间可以参与的秒杀商品信息
     * @return
     */
    @ResponseBody
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
    @ResponseBody
    @GetMapping("/skuInfo/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId")Long skuId){
        SeckillSkuRedisTo seckillSkuRedisTo = seckillSkuService.getSkuSeckillInfo(skuId);
        return R.ok().setData(seckillSkuRedisTo);
    }

    /***
     * 秒杀商品完整的业务逻辑 -》立即抢购
     * @param seckillId
     * @param key
     * @param num
     * @return
     */
    @GetMapping("/kill")
    public String seckill(@RequestParam("seckillId") String seckillId,
                          @RequestParam("key")String key,
                          @RequestParam("num")Integer num,
                          Model model){
        String orderSn = seckillSkuService.kill(seckillId,key,num);
        model.addAttribute("orderSn",orderSn);
        return "seckillSuccess";
    }
}
