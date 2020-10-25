package com.jerusalem.cart.service;

import com.jerusalem.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

/****
 * @Author: jerusalem
 * @Description: CartService业务层接口
 * @Date 2020/9/30 18:57
 *****/
public interface CartService {

    /***
     * 添加商品到购物车
     * @param skuId
     * @param num
     * @return
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;
}
