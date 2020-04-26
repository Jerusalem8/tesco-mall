package com.jerusalem.coupon.feign;

import com.jerusalem.common.to.SkuReductionTo;
import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author: jerusalem
 * @Description: SkuFullReductionFeign业务层接口
 * @Date 2020/4/24 18:10
 *****/
@FeignClient(name="coupon-service")
@RequestMapping("coupon/sku/full/reduction")
public interface SkuFullReductionFeign {

    /***
     * 保存SKU满减信息
     * @param skuReductionTo
     * @return
     */
    @PostMapping("/save")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
