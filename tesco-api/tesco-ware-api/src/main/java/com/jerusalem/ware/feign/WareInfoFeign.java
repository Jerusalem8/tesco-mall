package com.jerusalem.ware.feign;

import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/****
 * @Author: jerusalem
 * @Description: WareInfoFeign
 * @Date 2020/11/9 19:10
 *****/
@FeignClient("ware-service")
@RequestMapping("ware/ware/info")
public interface WareInfoFeign {

    /***
     * 根据收货地址id获取运费
     * @param addrId
     * @return
     */
    @GetMapping("/fare")
    R getFare(@RequestParam("addrId") Long addrId);
}
