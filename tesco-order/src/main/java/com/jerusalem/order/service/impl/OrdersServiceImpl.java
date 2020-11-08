package com.jerusalem.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.jerusalem.cart.feign.CartFeign;
import com.jerusalem.common.utils.R;
import com.jerusalem.common.vo.OrderItemVo;
import com.jerusalem.common.vo.SkuStockVo;
import com.jerusalem.common.vo.UserAddressVo;
import com.jerusalem.common.vo.UserResponseVo;
import com.jerusalem.order.constant.OrderTokenConstant;
import com.jerusalem.order.interceptor.LoginInterceptor;
import com.jerusalem.order.vo.OrderConfirmVo;
import com.jerusalem.user.feign.UserReceiveAddressFeign;
import com.jerusalem.ware.feign.WareSkuFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.order.dao.OrdersDao;
import com.jerusalem.order.entity.OrdersEntity;
import com.jerusalem.order.service.OrdersService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/****
 * 服务层接口实现类
 * 订单
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:49:06
 */
@Service("ordersService")
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, OrdersEntity> implements OrdersService {

    @Autowired
    UserReceiveAddressFeign userReceiveAddressFeign;

    @Autowired
    CartFeign cartFeign;

    @Autowired
    WareSkuFeign wareSkuFeign;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrdersEntity> page = this.page(
                new Query<OrdersEntity>().getPage(params),
                new QueryWrapper<OrdersEntity>()
        );
        return new PageUtils(page);
    }

    /***
     * 获取结算页封装信息
     * @return
     */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        //上下文环境的保持器,拿到刚进来的请求
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //异步编排两个远程调用任务
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            //副线程共享请求信息
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //1.远程查询用户的所有收货地址信息
            List<UserAddressVo> addressList = userReceiveAddressFeign.getAddress(userResponseVo.getId());
            orderConfirmVo.setUserAddressVos(addressList);
        }, threadPoolExecutor);
        CompletableFuture<Void> orderItemFuture = CompletableFuture.runAsync(() -> {
            //副线程共享请求信息
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //2.远程查询购物车选中的购物项
            //feign远程调用请求头丢失问题：添加拦截器，通信新老请求数据
            List<OrderItemVo> userCartItems = cartFeign.getUserCartItems();
            orderConfirmVo.setOrderItemVos(userCartItems);
        }, threadPoolExecutor).thenRunAsync(()->{
            //异步批量查询库存状态
            List<OrderItemVo> orderItemVos = orderConfirmVo.getOrderItemVos();
            //将所有商品的id封装成一个map
            List<Long> skuIdList = orderItemVos.stream().map(orderItemVo -> orderItemVo.getSkuId()).collect(Collectors.toList());
            R skuStock = wareSkuFeign.getSkuStock(skuIdList);
            List<SkuStockVo> skuStockVos = skuStock.getData(new TypeReference<List<SkuStockVo>>(){});
            if (skuStockVos != null){
                Map<Long, Boolean> skuStockMap = skuStockVos.stream().collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock));
                orderConfirmVo.setSkuStocks(skuStockMap);
            }
        },threadPoolExecutor);

        //3.查询用户积分
        Integer integration = userResponseVo.getIntegration();
        orderConfirmVo.setIntegration(integration);
        //4.其他数据自动计算
        //5.防重令牌(重点)
        String token = UUID.randomUUID().toString().replace("-", "");
        //存放到redis中，并设置过期时间
        String tokenKey = OrderTokenConstant.USER_ORDER_TOKEN_PREFIX+userResponseVo.getId();
        redisTemplate.opsForValue().set(token,token,30, TimeUnit.MINUTES);
        //同时给页面发一份
        orderConfirmVo.setOrderToken(token);
        //等待所有任务完成
        CompletableFuture.allOf(addressFuture,orderItemFuture).get();
        return orderConfirmVo;
    }
}