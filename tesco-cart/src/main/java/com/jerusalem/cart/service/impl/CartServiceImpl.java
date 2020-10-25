package com.jerusalem.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jerusalem.cart.interceptor.CartInterceptor;
import com.jerusalem.cart.service.CartService;
import com.jerusalem.cart.vo.CartItem;
import com.jerusalem.cart.vo.SkuInfoVo;
import com.jerusalem.cart.vo.UserInfoTo;
import com.jerusalem.common.utils.R;
import com.jerusalem.goods.feign.SkuInfoFeign;
import com.jerusalem.goods.feign.SkuSaleAttrValueFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

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

        //商品信息添加到购物项
        CartItem cartItem = new CartItem();

        //利用线程池异步编排任务
        //任务一
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
}
