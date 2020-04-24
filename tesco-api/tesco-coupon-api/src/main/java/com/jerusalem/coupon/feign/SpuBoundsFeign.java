package com.jerusalem.coupon.feign;

import com.jerusalem.common.to.SpuBoundsTo;
import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author: jerusalem
 * @Description: couponFeign
 * @Date 2020/4/22 18:19
 *****/
@FeignClient(name="coupon-service")
@RequestMapping("coupon/spu/bounds")
public interface SpuBoundsFeign {

    /***
     * 保存积分信息
     * @return
     */
    @PostMapping("/save")
    R save(@RequestBody SpuBoundsTo spuBoundsTo);
}
