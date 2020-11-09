package com.jerusalem.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jerusalem.cart.feign.CartFeign;
import com.jerusalem.common.utils.R;
import com.jerusalem.common.vo.OrderItemVo;
import com.jerusalem.common.vo.SkuStockVo;
import com.jerusalem.common.vo.UserAddressVo;
import com.jerusalem.common.vo.UserResponseVo;
import com.jerusalem.goods.feign.SpuInfoFeign;
import com.jerusalem.order.constant.OrderTokenConstant;
import com.jerusalem.order.entity.OrderItemEntity;
import com.jerusalem.order.interceptor.LoginInterceptor;
import com.jerusalem.order.to.OrderCreateTo;
import com.jerusalem.order.vo.*;
import com.jerusalem.user.feign.UserReceiveAddressFeign;
import com.jerusalem.ware.feign.WareInfoFeign;
import com.jerusalem.ware.feign.WareSkuFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
import org.springframework.util.StringUtils;
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

    /***
     * OrderSubmitVo
     */
    private ThreadLocal<OrderSubmitVo> submitVoThreadLocal = new ThreadLocal<>();

    @Autowired
    UserReceiveAddressFeign userReceiveAddressFeign;

    @Autowired
    CartFeign cartFeign;

    @Autowired
    WareSkuFeign wareSkuFeign;

    @Autowired
    WareInfoFeign wareInfoFeign;

    @Autowired
    SpuInfoFeign spuInfoFeign;

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

    /***
     * 提交订单
     * @param orderSubmitVo
     * @return
     */
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        //获取当前系统登录用户
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        //将前端传来的所有数据放入ThreadLocal共享
        submitVoThreadLocal.set(orderSubmitVo);
        //1.验证令牌【令牌的对比和删除必须保证原子性】【0-验证失败 1-验证成功】
        //验证脚本
        String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del'.KEYS[1]) else return 0 end";
        //redis中令牌的key
        String tokenKey = OrderTokenConstant.USER_ORDER_TOKEN_PREFIX + userResponseVo.getId();
        //前端传来的token
        String orderToken = orderSubmitVo.getOrderToken();
        //调用方法进行验证
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(tokenKey), orderToken);
        if (result == 0L){
            //验证失败，设置失败状态码，返回
            responseVo.setCode(1);
            return responseVo;
        }else {
            //验证成功，执行后续业务逻辑
            //2.创建订单
            OrderCreateTo order = createOrder();
        }
        return null;
    }

    /***
     * 抽取的公共方法
     * 创建订单
     * 1.订单
     * 2.订单项
     * @return
     */
    private OrderCreateTo createOrder(){
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        //生成订单号
        String orderSn = IdWorker.getTimeId();
        //1.根据订单号构建订单
        OrdersEntity order = buildOrder(orderSn);
        //2.获取所有的订单项
        List<OrderItemEntity> orderItems = buildOrderItems(orderSn);
        //TODO 3.验价(暂时只实现计算价格)
        computePrice(order,orderItems);
        return orderCreateTo;
    }


    /***
     * 抽取公共方法
     * 构建订单
     * @param orderSn
     * @return
     */
    private OrdersEntity buildOrder(String orderSn) {
        //获取共享数据
        OrderSubmitVo orderSubmitVo = submitVoThreadLocal.get();
        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setOrderSn(orderSn);
        //获取收货地址信息
        R r = wareInfoFeign.getFare(orderSubmitVo.getAddrId());
        AddressWithFareVo addrWithFareVo = r.getData(new TypeReference<AddressWithFareVo>() {
        });
        //设置各项数据
        ordersEntity.setFreightAmount(addrWithFareVo.getFare());
        ordersEntity.setReceiverCity(addrWithFareVo.getUserAddressVo().getCity());
        ordersEntity.setReceiverDetailAddress(addrWithFareVo.getUserAddressVo().getDetailAddress());
        ordersEntity.setReceiverName(addrWithFareVo.getUserAddressVo().getName());
        ordersEntity.setReceiverPhone(addrWithFareVo.getUserAddressVo().getPhone());
        ordersEntity.setReceiverPostCode(addrWithFareVo.getUserAddressVo().getPostCode());
        ordersEntity.setReceiverProvince(addrWithFareVo.getUserAddressVo().getProvince());
        ordersEntity.setReceiverRegion(addrWithFareVo.getUserAddressVo().getRegion());
        return ordersEntity;
    }

    /***
     * 抽取的公共方法
     * 构建订单项集合
     * @return
     * @param orderSn
     */
    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemVo> currentCartItems = cartFeign.getUserCartItems();
        if (currentCartItems != null && currentCartItems.size()>0){
            List<OrderItemEntity> orderItems = currentCartItems.stream().map(currentCartItem -> {
                OrderItemEntity orderItem = buildOrderItem(currentCartItem);
                orderItem.setOrderSn(orderSn);
                return orderItem;
            }).collect(Collectors.toList());
            return orderItems;
        }
        return null;
    }

    /***
     * 抽取的公共方法
     * 构建订单项
     * 1.订单号 2.SPU信息 3.SKU信息 4.优惠信息（暂不实现） 5。积分信息
     * @param orderItemVo
     * @return
     */
    private OrderItemEntity buildOrderItem(OrderItemVo orderItemVo) {
        OrderItemEntity itemEntity = new OrderItemEntity();
        //2.SPU信息
        Long skuId = orderItemVo.getSkuId();
        R r = spuInfoFeign.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoVo = r.getData(new TypeReference<SpuInfoVo>() {
        });
        itemEntity.setSpuId(spuInfoVo.getId());
        itemEntity.setSpuBrand(spuInfoVo.getBrandId().toString());
        itemEntity.setSpuName(spuInfoVo.getSpuName());
        itemEntity.setCategoryId(spuInfoVo.getCategoryId());
        //3.SKU信息
        itemEntity.setSkuId(orderItemVo.getSkuId());
        itemEntity.setSkuName(orderItemVo.getTitle());
        itemEntity.setSkuPic(orderItemVo.getImage());
        itemEntity.setSkuPrice(orderItemVo.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(orderItemVo.getSkuAttr(), ";");
        itemEntity.setSkuAttrsVals(skuAttr);
        itemEntity.setSkuQuantity(orderItemVo.getCount());
        //5.积分信息（暂时以价格作为成长积分）
        itemEntity.setGiftGrowth(orderItemVo.getPrice().intValue());
        itemEntity.setGiftIntegration(orderItemVo.getPrice().intValue());
        return itemEntity;
    }

    /***
     * 抽取公共方法
     * 计算价格
     * @param order
     * @param orderItems
     */
    private void computePrice(OrdersEntity order, List<OrderItemEntity> orderItems) {
    }
}