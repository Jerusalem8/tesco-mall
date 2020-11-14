package com.jerusalem.order.feign;

import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author: jerusalem
 * @Description: OrdersFeign业务层接口
 * @Date 2020/11/14 17:07
 *****/
@FeignClient(name="order-service")
@RequestMapping("order/orders")
public interface OrdersFeign {

    /***
     * 根据订单号查询订单信息
     * @param orderSn
     * @return
     */
    @GetMapping("/status/{orderSn}")
    R getOrderStatus(@PathVariable("orderSn") String orderSn);
}
