package com.jerusalem.cart.controller;

import com.jerusalem.cart.interceptor.CartInterceptor;
import com.jerusalem.cart.service.CartService;
import com.jerusalem.cart.vo.CartItem;
import com.jerusalem.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;

/****
 * @Author: jerusalem
 * @Description: CartController
 * @Date 2020/9/30 19:07
 *****/
@Controller
public class CartController {

    @Autowired
    CartService cartService;

    /***
     * 获取购物车页面
     * 关于临时购物车、用户购物车的做法
     * 浏览器有一个cookie:user-key:标识用户唯一身份（有效期为一个月）
     * ThreadLocal（用户身份鉴别） - 同一个线程共享数据
     * @return
     */
    @GetMapping("/cartList.html")
    public String cartListPage(){
        //快速得到用户信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println(userInfoTo);
        return "cartList";
    }

    /***
     * 添加商品到购物车
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num")Integer num, Model model) throws ExecutionException, InterruptedException {
        CartItem cartItem = cartService.addToCart(skuId,num);
        model.addAttribute("item",cartItem);
        return "success";
    }
}
