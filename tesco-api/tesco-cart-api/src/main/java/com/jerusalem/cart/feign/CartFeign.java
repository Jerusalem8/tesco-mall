package com.jerusalem.cart.feign;

import com.jerusalem.common.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: CartFeign业务层接口
 * @Date 2020/11/7 14:05
 *****/
@FeignClient(name="cart-service")
public interface CartFeign {

    /***
     * 获取当前登录用户的所有选中的购物项
     * @return
     */
    @GetMapping("/cartItems")
    List<OrderItemVo> getUserCartItems();
}
