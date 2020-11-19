package com.jerusalem.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jerusalem.cart.feign.CartFeign;
import com.jerusalem.common.enume.OrderStatusEnum;
import com.jerusalem.common.exception.NoStockException;
import com.jerusalem.common.to.OrderTo;
import com.jerusalem.common.to.SeckillOrderTo;
import com.jerusalem.common.utils.R;
import com.jerusalem.common.vo.*;
import com.jerusalem.goods.feign.SpuInfoFeign;
import com.jerusalem.order.entity.PaymentInfoEntity;
import com.jerusalem.order.enume.OrderCodeEnume;
import com.jerusalem.order.constant.OrderTokenConstant;
import com.jerusalem.order.entity.OrderItemEntity;
import com.jerusalem.order.interceptor.LoginInterceptor;
import com.jerusalem.order.service.OrderItemService;
import com.jerusalem.order.service.PaymentInfoService;
import com.jerusalem.order.to.OrderCreateTo;
import com.jerusalem.order.vo.*;
import com.jerusalem.user.feign.UserReceiveAddressFeign;
import com.jerusalem.ware.feign.WareInfoFeign;
import com.jerusalem.ware.feign.WareSkuFeign;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    PaymentInfoService paymentInfoService;

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
        redisTemplate.opsForValue().set(tokenKey,token,30, TimeUnit.MINUTES);
        //同时给页面发一份
        orderConfirmVo.setOrderToken(token);
        //等待所有任务完成
        CompletableFuture.allOf(addressFuture,orderItemFuture).get();
        return orderConfirmVo;
    }

    /***
     * 提交订单
     * TODO 本地事务在分布式下的问题 - 本地事务在分布式系统中，只能控制住自己的回滚，控制不了其他服务的回滚
     * TODO 分布式事务的回滚机制
     * TODO 分布式事务的最大原因：网络问题
     * 高并发场景 - 不使用2PC模式，引入MQ
     * @param orderSubmitVo
     * @return
     */
    @Transactional  //本地事务
//    @GlobalTransactional   //分布式事务
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        //获取当前系统登录用户
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        //初始状态码0，出现问题是状态码改变
        responseVo.setCode(0);
        //将前端传来的所有数据放入ThreadLocal共享
        submitVoThreadLocal.set(orderSubmitVo);
        //1.验证令牌【令牌的对比和删除必须保证原子性】【0-验证失败 1-验证成功】
        //验证脚本
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        //redis中令牌的key
        String tokenKey = OrderTokenConstant.USER_ORDER_TOKEN_PREFIX + userResponseVo.getId();
        //前端传来的token
        String orderToken = orderSubmitVo.getOrderToken();
        //调用方法进行验证
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(tokenKey), orderToken);
        if (result == 0L){
            //验证失败，设置失败状态码，返回
            responseVo.setCode(OrderCodeEnume.TOKEN_EXCEPTION.getCode());
            return responseVo;
        }else {
            //验证成功，执行后续业务逻辑
            //2.创建订单
            OrderCreateTo order = createOrder();
            //3.验价
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = orderSubmitVo.getPayPrice();
            //取两者的绝对值，绝对值小于0.01即可，无需完全相等
            double abs = Math.abs(payAmount.subtract(payPrice).doubleValue());
            if (abs < 0.01){
                //4.金额对比成功，保存订单
                saveOrder(order);
                //5.远程锁定库存【只要有异常，回滚订单数据】【重点内容】
                //封装锁定库存所需要的信息
                LockStockVo lockStockVo = new LockStockVo();
                lockStockVo.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVo> locks = order.getOrderItems().stream().map(item -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).collect(Collectors.toList());
                lockStockVo.setOrderItemLocks(locks);
                R r = wareSkuFeign.lockStock(lockStockVo);
                if (r.getCode() == 0){
                    //锁定成功,返回订单，发送消息给mq
                    responseVo.setOrder(order.getOrder());
                    //模拟测试bug
//                    int i = 10/0;
                    rabbitTemplate.convertAndSend("order-event-exchange","order.create",order.getOrder());
                    return responseVo;
                }else {
                    //锁定库存失败，抛异常回滚
                    String msg = (String) r.get("msg");
                    throw new NoStockException(msg);
                }
                //TODO 6.远程扣减积分
            }else{
                //金额对比失败
                responseVo.setCode(OrderCodeEnume.PRICE_EXCEPTION.getCode());
                return responseVo;
            }
        }
    }

    /***
     * 根据订单号查询订单信息
     * @param orderSn
     * @return
     */
    @Override
    public OrdersEntity getOrderByOrderSn(String orderSn) {
        OrdersEntity ordersEntity = this.getOne(new QueryWrapper<OrdersEntity>().eq("order_sn", orderSn));
        return ordersEntity;
    }

    /***
     * 关闭订单
     * @param ordersEntity
     */
    @Override
    public void closeOrder(OrdersEntity ordersEntity) {
        OrdersEntity entity = this.getById(ordersEntity.getId());
        //待付款状态，关闭订单
        if (entity.getStatus() == OrderStatusEnum.CREATE_NEW.getCode()){
            OrdersEntity updateEntity = new OrdersEntity();
            updateEntity.setId(entity.getId());
            updateEntity.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(updateEntity);
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(entity,orderTo);
            //再次发消息给mq，以确保库存解锁
            rabbitTemplate.convertAndSend("order-event-exchange","order.release.other",orderTo);
        }
    }

    /***
     * 获取订单的支付信息
     * @param orderSn
     * @return
     */
    @Override
    public PayVo getPayOrder(String orderSn) {
        PayVo payVo = new PayVo();
        OrdersEntity ordersEntity = this.getOrderByOrderSn(orderSn);
        //对应付金额进行格式处理（向上取值，保留两位小数）
        BigDecimal totalAmount = ordersEntity.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        payVo.setTotal_amount(totalAmount.toString());
        payVo.setOut_trade_no(ordersEntity.getOrderSn());
        List<OrderItemEntity> list = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderSn));
        OrderItemEntity itemEntity = list.get(0);
        String skuName = itemEntity.getSkuName();
        payVo.setSubject(skuName);//订单主题(将该订单的第一个订单项的名字作为订单主题)
        payVo.setBody("七天无理由退货");//订单备注（简单处理）
        return payVo;
    }

    /****
     * 分页查询订单列表及订单项
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        IPage<OrdersEntity> page = this.page(
                new Query<OrdersEntity>().getPage(params),
                new QueryWrapper<OrdersEntity>().eq("member_id",userResponseVo.getId()).orderByDesc("id")
        );
        List<OrdersEntity> ordersEntities = page.getRecords().stream().map(order -> {
            List<OrderItemEntity> itemEntityList = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", order.getOrderSn()));
            order.setItemEntityList(itemEntityList);
            return order;
        }).collect(Collectors.toList());
        page.setRecords(ordersEntities);
        return new PageUtils(page);
    }

    /***
     * 处理支付回调通知
     * @param payAsyncVo
     * @return
     */
    @Override
    public String handlePayResult(PayAsyncVo payAsyncVo) {
        //1.保存交易流水
        PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
        paymentInfoEntity.setAlipayTradeNo(payAsyncVo.getTrade_no());
        paymentInfoEntity.setOrderSn(payAsyncVo.getOut_trade_no());
        paymentInfoEntity.setPaymentStatus(payAsyncVo.getTrade_status());
        paymentInfoEntity.setCallbackTime(payAsyncVo.getNotify_time());
        paymentInfoService.save(paymentInfoEntity);
        //2.修改订单的状态
        if (payAsyncVo.getTrade_status().equals("TRADE_SUCCESS") || payAsyncVo.getTrade_status().equals("TRADE_FINISHED")){
            String orderSn = payAsyncVo.getOut_trade_no();
            UpdateWrapper<OrdersEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status",OrderStatusEnum.PAYED.getCode());
            updateWrapper.eq("order_sn",orderSn);
            this.update(updateWrapper);
        }
        return "success";
    }

    /****
     * 创建秒杀订单的详细信息
     * //TODO 保存秒杀订单的详细信息 订单、订单项（此处简略设置几项属性即可）
     * @param seckillOrderTo
     */
    @Override
    public void createSeckillOrder(SeckillOrderTo seckillOrderTo) {
        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setOrderSn(seckillOrderTo.getOrderSn());
        ordersEntity.setMemberId(seckillOrderTo.getUserId());
        ordersEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        BigDecimal payAmount = seckillOrderTo.getSeckillPrice().multiply(new BigDecimal("" + seckillOrderTo.getNum()));
        ordersEntity.setPayAmount(payAmount);
        this.save(ordersEntity);
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderItemEntity.setRealAmount(payAmount);
        orderItemEntity.setSkuQuantity(seckillOrderTo.getNum());
        orderItemService.save(orderItemEntity);
    }

    /***
     * 保存订单
     * @param order
     */
    private void saveOrder(OrderCreateTo order) {
        OrdersEntity ordersEntity = order.getOrder();
        ordersEntity.setModifyTime(new Date());
        this.save(ordersEntity);
        List<OrderItemEntity> orderItems = order.getOrderItems();
        orderItemService.saveBatch(orderItems);
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
        //3.计算价格及价格信息设置
        computePrice(order,orderItems);
        orderCreateTo.setOrder(order);
        orderCreateTo.setOrderItems(orderItems);
        return orderCreateTo;
    }

    /***
     * 抽取公共方法
     * 构建订单
     * @param orderSn
     * @return
     */
    private OrdersEntity buildOrder(String orderSn) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        //获取共享数据
        OrderSubmitVo orderSubmitVo = submitVoThreadLocal.get();
        OrdersEntity ordersEntity = new OrdersEntity();
        //设置订单号
        ordersEntity.setOrderSn(orderSn);
        //设置用户Id
        ordersEntity.setMemberId(userResponseVo.getId());
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
        //设置订单状态信息
        ordersEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        //默认确认时间
        ordersEntity.setAutoConfirmDay(7);
        //默认未删除
        ordersEntity.setDeleteStatus(0);
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
     * 1.订单号 2.SPU信息 3.SKU信息 4.优惠信息（暂不实现） 5。积分信息 6.价格
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
        itemEntity.setGiftGrowth(orderItemVo.getPrice().multiply(new BigDecimal(orderItemVo.getCount().toString())).intValue());
        itemEntity.setGiftIntegration(orderItemVo.getPrice().multiply(new BigDecimal(orderItemVo.getCount().toString())).intValue());
        //6.价格
        itemEntity.setPromotionAmount(new BigDecimal("0"));
        itemEntity.setCouponAmount(new BigDecimal("0"));
        itemEntity.setIntegrationAmount(new BigDecimal("0"));
        //总金额
        BigDecimal orderPrice = itemEntity.getSkuPrice().multiply(new BigDecimal(itemEntity.getSkuQuantity().toString()));
        //实际金额 = 总金额 - 各项优惠
        BigDecimal realAmount = orderPrice
                .subtract(itemEntity.getCouponAmount())
                .subtract(itemEntity.getPromotionAmount())
                .subtract(itemEntity.getIntegrationAmount());
        itemEntity.setRealAmount(realAmount);
        return itemEntity;
    }

    /***
     * 抽取公共方法
     * 计算价格及价格信息设置
     * @param order
     * @param orderItems
     */
    private void computePrice(OrdersEntity order, List<OrderItemEntity> orderItems) {
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal integration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");
        BigDecimal giftGrowth = new BigDecimal("0.0");
        BigDecimal giftIntegration = new BigDecimal("0.0");
        //订单的各种总额，叠加每一个订单项的价格信息
        for (OrderItemEntity orderItem : orderItems) {
            total = total.add(orderItem.getRealAmount());
            coupon = coupon.add(orderItem.getCouponAmount());
            integration = integration.add(orderItem.getIntegrationAmount());
            promotion = promotion.add(orderItem.getPromotionAmount());
            giftGrowth = giftGrowth.add(new BigDecimal(orderItem.getGiftGrowth().toString()));
            giftIntegration = giftIntegration.add(new BigDecimal(orderItem.getGiftIntegration().toString()));
        }
        //填充各种价格信息
        order.setTotalAmount(total);
        //实际价格 = 总价格 - 运费
        order.setPayAmount(total.add(order.getFreightAmount()));
        order.setPromotionAmount(promotion);
        order.setIntegrationAmount(integration);
        order.setCouponAmount(coupon);
        //设置积分信息
        order.setIntegration(giftIntegration.intValue());
        order.setGrowth(giftGrowth.intValue());
    }
}