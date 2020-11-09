package com.jerusalem.order.controller;

import com.jerusalem.order.service.OrdersService;
import com.jerusalem.order.vo.OrderConfirmVo;
import com.jerusalem.order.vo.OrderSubmitVo;
import com.jerusalem.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.ExecutionException;

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
     * 去结算页
     * @return
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = ordersService.confirmOrder();
        model.addAttribute("orderConfirm",orderConfirmVo);
        return "confirm";
    }

    /***
     * 提交订单（下单功能）
     * @param orderSubmitVo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmitVo){
        SubmitOrderResponseVo orderResponse = ordersService.submitOrder(orderSubmitVo);
        if (orderResponse.getCode() == 0){
            //下单成功跳转到支付页
            return "pay";
        }else {
            //下单失败重定向到订单结算页
            return "redirect:http://order.tesco.com/toTrade";
        }
    }
}
