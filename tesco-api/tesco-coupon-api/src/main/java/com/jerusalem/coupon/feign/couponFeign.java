package com.jerusalem.coupon.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author: jerusalem
 * @Description: couponFeign
 * @Date 2020/4/22 18:19
 *****/
@FeignClient(name="coupon-service")
@RequestMapping("coupon/")
public interface couponFeign {
}
