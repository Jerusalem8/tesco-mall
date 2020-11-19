package com.jerusalem.seckill.service;

import com.jerusalem.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SeckillSkuService业务层接口
 * @Date 2020/11/17 11:14
 *****/
public interface SeckillSkuService {

    /***
     * 上架最近三天的秒杀商品
     */
    void upSeckillSkuLatest3Days();

    /***
     * 获取当前时间可以参与的秒杀商品信息
     * @return
     */
    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    /***
     * 获取sku的秒杀信息
     * 查询是否是秒杀商品
     * @param skuId
     * @return
     */
    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    /***
     * 秒杀商品完整的业务逻辑 -》立即抢购
     * @param seckillId
     * @param key
     * @param num
     * @return
     */
    String kill(String seckillId, String key, Integer num);
}
