package com.jerusalem.goods.feign;

import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author: jerusalem
 * @Description: AttrFeign业务层接口
 * @Date 2020/8/2 15:41
 *****/
@FeignClient(name="goods-service")
@RequestMapping("goods/attr")
public interface AttrFeign {
    /***
     * 查询属性信息（实现修改时的数据回显）
     * @return
     */
    @GetMapping("/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);
}
