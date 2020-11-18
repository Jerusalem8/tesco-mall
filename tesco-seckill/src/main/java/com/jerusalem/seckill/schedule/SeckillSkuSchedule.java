package com.jerusalem.seckill.schedule;

import com.jerusalem.seckill.service.SeckillSkuService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/****
 * @Author: jerusalem
 * @Description: SeckillSkuSchedule
 * 秒杀商品上架定时任务
 * @Date 2020/11/17 11:04
 *****/
@Slf4j
@Service
public class SeckillSkuSchedule {

    @Autowired
    SeckillSkuService seckillSkuService;

    @Autowired
    RedissonClient redissonClient;

    private final String upload_lock = "seckill:upload:lock";

    /***
     * 上架最近三天的秒杀商品
     * 1.加锁
     * 2.幂等性保证，解决重复上架问题
     */
    @Scheduled(cron = "0 * * * * ?")
    public void upSeckillSkuLatest3Days(){
//        log.info("上架秒杀商品");
        //设置分布式锁，得到锁的才执行
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try{
            seckillSkuService.upSeckillSkuLatest3Days();
        }finally {
            lock.unlock();
        }
    }
}
