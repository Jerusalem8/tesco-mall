package com.jerusalem.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jerusalem.common.utils.R;
import com.jerusalem.coupon.feign.SeckillSessionFeign;
import com.jerusalem.goods.feign.SkuInfoFeign;
import com.jerusalem.seckill.service.SeckillSkuService;
import com.jerusalem.seckill.to.SeckillSkuRedisTo;
import com.jerusalem.seckill.vo.SeckillSessionWithSkusVo;
import com.jerusalem.seckill.vo.SkuDetailInfoVo;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/****
 * @Author: jerusalem
 * @Description: SeckillSkuService
 * @Date 2020/11/17 11:15
 *****/
@Service
public class SeckillSkuServiceImpl implements SeckillSkuService {

    @Autowired
    SeckillSessionFeign seckillSessionFeign;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    SkuInfoFeign skuInfoFeign;

    @Autowired
    RedissonClient redissonClient;

    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";
    private final String SKUS_CACHE_PREFIX = "seckill:skus:";
    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";//+商品随机码

    /***
     * 上架最近三天的秒杀商品
     */
    @Override
    public void upSeckillSkuLatest3Days() {
        //1.查询最近三天需要参与秒杀的商品
        R r = seckillSessionFeign.getLatest3DaysSession();
        if (r.getCode() == 0){
            //拿到数据
            List<SeckillSessionWithSkusVo> sessionWithSkusList = r.getData(new TypeReference<List<SeckillSessionWithSkusVo>>() {
            });
            //2.缓存到Redis
            //2.1 缓存活动信息
            saveSessionInfos(sessionWithSkusList);
            //2.2 缓存活动关联的商品信息
            saveSeckillSkuInfos(sessionWithSkusList);
        }
    }

    /***
     * 获取当前时间可以参与的秒杀商品信息
     * .getTime()：获得数字时间
     * @return
     */
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        //1.确定当前时间的秒杀场次
        long time = new Date().getTime();
        Set<String> keys = stringRedisTemplate.keys(SESSION_CACHE_PREFIX + "*");
        for (String key : keys) {
            String replace = key.replace(SESSION_CACHE_PREFIX, "");
            String[] s = replace.split("_");
            long startTime = Long.parseLong(s[0]);
            long endTime = Long.parseLong(s[1]);
            if (time>=startTime && time<=endTime){
                //2.查询该秒杀场次的秒杀商品
                List<String> range = stringRedisTemplate.opsForList().range(key, -10000, 10000);
                BoundHashOperations<String, String, String> hashOps =  stringRedisTemplate.boundHashOps(SKUS_CACHE_PREFIX);
                List<String> list = hashOps.multiGet(range);
                if (list != null){
                    List<SeckillSkuRedisTo> collect = list.stream().map(item -> {
                        SeckillSkuRedisTo redisTo = JSON.parseObject(item.toString(), SeckillSkuRedisTo.class);
//                        redisTo.setRandomCode(null);
                        return redisTo;
                    }).collect(Collectors.toList());
                    return collect;
                }
                break;
            }
        }
        return null;
    }

    /***
     * 获取sku的秒杀信息
     * 查询是否是秒杀商品
     * @param skuId
     * @return
     */
    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        //找到所有需要参与秒杀的商品的key
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUS_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        if (keys!=null && keys.size()>0){
            //正则匹配
            String regx = "\\d-"+skuId;
            for (String key : keys) {
                //从redis中拿到秒杀信息
                if (Pattern.matches(regx,key)){
                    String json = hashOps.get(key);
                    SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
                    //随机码的处理（只有正处于秒杀时间内时，才返回随机码）
                    long time = new Date().getTime();
                    if (time>=seckillSkuRedisTo.getStartTime()&&time<=seckillSkuRedisTo.getEndTime()){
                    }else {
                        //否则，随机码置为空
                        seckillSkuRedisTo.setRandomCode(null);
                    }
                    return seckillSkuRedisTo;
                }
            }
        }
        return null;
    }

    /***
     * 缓存活动信息
     * @param list
     */
    private void saveSessionInfos(List<SeckillSessionWithSkusVo> list){
        list.stream().forEach(session ->{
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();
            String key = SESSION_CACHE_PREFIX + startTime+"_"+endTime;
            //进行幂等性保证，如果已存在，不再缓存
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            //TODO bug：已经缓存的秒杀活动，新增关联商品，无法缓存
            if (!hasKey){
                List<String> collect = session.getRelationSkus().stream().map(item -> item.getPromotionSessionId().toString()+"-"+item.getSkuId()).collect(Collectors.toList());
                stringRedisTemplate.opsForList().leftPushAll(key,collect);
            }
        });
    }

    /**
     * 缓存秒杀商品
     * @param list
     */
    private void saveSeckillSkuInfos(List<SeckillSessionWithSkusVo> list){
        list.stream().forEach(session->{
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(SKUS_CACHE_PREFIX);
            session.getRelationSkus().stream().forEach(seckillSkuVo -> {
                Boolean hasKey = ops.hasKey(seckillSkuVo.getPromotionSessionId().toString()+"-"+seckillSkuVo.getSkuId().toString());
                if (!hasKey){
                    SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                    //1.缓存秒杀商品的详细信息
                    R r = skuInfoFeign.getSkuInfo(seckillSkuVo.getSkuId());
                    if (r.getCode() == 0){
                        SkuDetailInfoVo skuInfo = r.getData("skuInfo", new TypeReference<SkuDetailInfoVo>() {
                        });
                        redisTo.setSkuInfo(skuInfo);
                    }
                    //2.缓存sku的秒杀信息
                    BeanUtils.copyProperties(seckillSkuVo,redisTo);
                    //3.时间信息
                    redisTo.setStartTime(session.getStartTime().getTime());
                    redisTo.setEndTime(session.getEndTime().getTime());
                    //4.设置随机码(安全)
                    String code = UUID.randomUUID().toString().replace("-", "");
                    redisTo.setRandomCode(code);
                    //将以上redisTo转为json存入redis
                    String jsonString = JSON.toJSONString(redisTo);
                    ops.put(seckillSkuVo.getPromotionSessionId().toString()+"-"+seckillSkuVo.getSkuId().toString(),jsonString);
                    //5.设置分布式信号量，商品的秒杀最大件数作为信号量 -> 限流
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + code);
                    semaphore.trySetPermits(seckillSkuVo.getSeckillCount().intValue());
                }
            });
        });
    }
}
