package com.jerusalem.cart.service;

import com.jerusalem.cart.vo.Cart;
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

    /***
     * 查询购物车中的某个购物项
     * @param skuId
     * @return
     */
    CartItem getCartItem(Long skuId);

    /***
     * 获取购物车
     * @return
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /***
     * 清空购物车
     * @param userKey
     */
    void clearCart(String userKey);

    /***
     * 购物项选中状态的改变
     * @param skuId
     * @param check
     */
    void checkItem(Long skuId, Integer check);

    /***
     * 购物项数量的改变
     * @param skuId
     * @param num
     * @return
     */
    void changeItemCount(Long skuId, Integer num);

    /***
     * 删除购物项
     * @param skuId
     * @return
     */
    void deleteItem(Long skuId);
}
