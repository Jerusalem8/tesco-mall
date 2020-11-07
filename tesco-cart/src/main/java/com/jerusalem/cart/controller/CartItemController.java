package com.jerusalem.cart.controller;

import com.jerusalem.cart.service.CartService;
import com.jerusalem.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: CartItemController
 * @Date 2020/11/7 16:08
 *****/
@RestController
@RequestMapping("cart")
public class CartItemController {

    @Autowired
    CartService cartService;

    /***
     * 获取当前登录用户的所有选中的购物项
     * @return
     */
    @GetMapping("/cartItems")
    public List<CartItem> getUserCartItems(){
        List<CartItem> cartItems = cartService.getUserCartItems();
        return cartItems;
    }
}
