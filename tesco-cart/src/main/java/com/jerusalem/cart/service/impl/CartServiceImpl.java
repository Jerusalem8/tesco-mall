package com.jerusalem.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jerusalem.cart.interceptor.CartInterceptor;
import com.jerusalem.cart.service.CartService;
import com.jerusalem.cart.vo.Cart;
import com.jerusalem.cart.vo.CartItem;
import com.jerusalem.cart.vo.SkuInfoVo;
import com.jerusalem.cart.vo.UserInfoTo;
import com.jerusalem.common.utils.R;
import com.jerusalem.goods.feign.SkuInfoFeign;
import com.jerusalem.goods.feign.SkuSaleAttrValueFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/****
 * @Author: jerusalem
 * @Description: CartServiceImpl
 * @Date 2020/9/30 18:57
 *****/
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SkuInfoFeign skuInfoFeign;

    @Autowired
    SkuSaleAttrValueFeign skuSaleAttrValueFeign;

    @Autowired
    ThreadPoolExecutor executor;

    private final String CART_PREFIX = "tesco：cart：";

    /***
     * 添加商品到购物车
     * @param skuId
     * @param num
     * @return
     */
    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String result = (String) cartOps.get(skuId.toString());
        if (StringUtils.isEmpty(result)){
            //说明购物车之前无此商品，添加新商品信息到购物项
            //利用线程池异步编排任务
            //任务一
            CartItem cartItem = new CartItem();
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                //远程查询当前添加商品的详细信息
                R skuInfo = skuInfoFeign.getSkuInfo(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());
            }, executor);
            //任务二
            CompletableFuture<Void> getSkuSaleAttrValue = CompletableFuture.runAsync(() -> {
                //远程查询sku的组合信息
                List<String> skuSaleAttrValues = skuSaleAttrValueFeign.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(skuSaleAttrValues);
            }, executor);
            //确保任务都完成
            CompletableFuture.allOf(getSkuInfoTask,getSkuSaleAttrValue).get();
            //json序列化，方便存储
            String cartItemJson = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(),cartItemJson);
            return cartItem;
        }else {
            //购物车有此商品，只需修改商品数量
            CartItem cartItem = JSON.parseObject(result, CartItem.class);
            cartItem.setCount(cartItem.getCount()+num);
            cartOps.put(skuId.toString(),JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    /***
     * 查询购物车中的某个购物项
     * @param skuId
     * @return
     */
    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String result = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(result, CartItem.class);
        return cartItem;
    }

    /***
     * 获取购物车
     * @return
     */
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId()!=null){
            //1.已登录
            String userId = CART_PREFIX + userInfoTo.getUserId();
            String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            BoundHashOperations<String, Object, Object> cartOps = redisTemplate.boundHashOps(userId);
            //1.1 先获取、合并临时购物车
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if (tempCartItems != null){
                for (CartItem tempCartItem : tempCartItems) {
                    addToCart(tempCartItem.getSkuId(),tempCartItem.getCount());
                }
                //清空临时购物车的数据
                clearCart(tempCartKey);
            }
            //1.2 获取登陆后的购物车数据【包括合并后的临时后的临时购物车的数据】
            List<CartItem> cartItems = getCartItems(userId);
            cart.setCartItems(cartItems);
        }else {
            //2.没登录
            String userKey = CART_PREFIX + userInfoTo.getUserKey();
            //2.1 获取临时购物车的所有购物项
            List<CartItem> cartItems = getCartItems(userKey);
            cart.setCartItems(cartItems);
        }
        return cart;
    }

    /***
     * 清空购物车
     * @param userKey
     */
    @Override
    public void clearCart(String userKey) {
        redisTemplate.delete(userKey);
    }

    /***
     * 购物项选中状态的改变
     * @param skuId
     * @param check
     */
    @Override
    public void checkItem(Long skuId, Integer check) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check==1?true:false);
        String cartItemString = JSON.toJSONString(cartItem);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(),cartItemString);
    }

    /***
     * 购物项数量的改变
     * @param skuId
     * @param num
     * @return
     */
    @Override
    public void changeItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        String cartItemString = JSON.toJSONString(cartItem);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(),cartItemString);
    }

    /***
     * 删除购物项
     * @param skuId
     * @return
     */
    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }



    /***
     * 抽取的公共方法
     * 获取购物车
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        //快速得到用户信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null){
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        }else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }

    /***
     * 获取购物车的购物项
     * @param userKey
     * @return
     */
    private List<CartItem> getCartItems(String userKey){
        BoundHashOperations<String, Object, Object> cartOps = redisTemplate.boundHashOps(userKey);
        List<Object> values = cartOps.values();
        if (values!=null&&values.size()>0){
            List<CartItem> collect = values.stream().map((obj) -> {
                String str = (String) obj;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }
}
