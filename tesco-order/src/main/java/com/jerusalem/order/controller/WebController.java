package com.jerusalem.order.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.jerusalem.common.exception.NoStockException;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.order.config.AlipayTemplate;
import com.jerusalem.order.enume.OrderCodeEnume;
import com.jerusalem.order.service.OrdersService;
import com.jerusalem.order.vo.OrderConfirmVo;
import com.jerusalem.order.vo.OrderSubmitVo;
import com.jerusalem.order.vo.PayVo;
import com.jerusalem.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    AlipayTemplate alipayTemplate;

    /***
     * 跳转到结算页
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
    public String submitOrder(OrderSubmitVo orderSubmitVo, Model model, RedirectAttributes redirectAttributes){
        try{
            SubmitOrderResponseVo orderResponse = ordersService.submitOrder(orderSubmitVo);
            if (orderResponse.getCode() == 0){
                //下单成功跳转到支付页
                model.addAttribute("submitOrderResponse",orderResponse);
                return "pay";
            }else {
                //下单失败重定向到订单结算页
                Integer code = orderResponse.getCode();
                switch (code){
                    case 1:
                        redirectAttributes.addFlashAttribute("msg", OrderCodeEnume.TOKEN_EXCEPTION.getMsg()); break;
                    case 2:
                        redirectAttributes.addFlashAttribute("msg",OrderCodeEnume.PRICE_EXCEPTION.getMsg()); break;
                    case 3:
                        redirectAttributes.addFlashAttribute("msg",OrderCodeEnume.STOCK_EXCEPTION.getMsg()); break;
                }
                return "redirect:http://order.tesco.com/toTrade";
            }
        }catch (Exception e){
            if (e instanceof NoStockException){
                String message = ((NoStockException) e).getMessage();
                redirectAttributes.addFlashAttribute("msg",message);
            }
        }
        return "redirect:http://order.tesco.com/toTrade";
    }

    /***
     * 支付订单
     * @param orderSn
     * @return
     */
    @ResponseBody()
    @GetMapping(value = "/payOrder",produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVo payVo = ordersService.getPayOrder(orderSn);
        //返回的是一个页面
        String result = alipayTemplate.pay(payVo);
        return result;
    }


    /***
     * 支付成功，跳转到订单列表页
     * 获取当前登录用户的订单列表及订单项
     * @return
     */
    @GetMapping("/orderList.html")
    public String orderListPage(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum, Model model){
        Map<String, Object> page = new HashMap<>();
        page.put("page",pageNum.toString());
        PageUtils pageResult = ordersService.queryPageWithItem(page);
        model.addAttribute("orders",pageResult);
        return "orderList";
    }
}
