package com.jerusalem.coupon.feign;

import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author: jerusalem
 * @Description: SeckillSkuFeign业务层接口
 * @Date 2020/11/17 11:23
 *****/
@FeignClient(name="coupon-service")
@RequestMapping("coupon/seckillsession")
public interface SeckillSessionFeign {

    /***
     * 获取最近三天的秒杀商品
     * @return
     */
    @GetMapping("/latest3DaysSession")
    R getLatest3DaysSession();
}
