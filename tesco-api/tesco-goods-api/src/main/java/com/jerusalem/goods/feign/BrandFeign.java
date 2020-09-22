package com.jerusalem.goods.feign;

import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: BrandFeign业务层接口
 * @Date 2020/9/15 18:07
 *****/
@FeignClient(name="goods-service")
@RequestMapping("goods/brand")
public interface BrandFeign {
    /***
     * 批量查询品牌信息（筛选栏查询）
     * @param brandIds
     * @return
     */
    @GetMapping("/infos")
    R infos(@RequestParam("brandIds") List<Long> brandIds);
}
