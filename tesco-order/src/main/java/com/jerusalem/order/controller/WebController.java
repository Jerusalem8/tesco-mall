package com.jerusalem.order.controller;

import com.jerusalem.order.service.OrdersService;
import com.jerusalem.order.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/****
 * @Author: jerusalem
 * @Description: OrderWebController
 * 订单服务前端控制器
 * @Date 2020/11/7 12:26
 *****/
@Controller
public class WebController {

    @Autowired
    OrdersService ordersService;

    /***
     * 去结算
     * @return
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model){
        OrderConfirmVo orderConfirmVo = ordersService.confirmOrder();
        model.addAttribute("orderConfirm",orderConfirmVo);
        return "confirm";
    }
}
