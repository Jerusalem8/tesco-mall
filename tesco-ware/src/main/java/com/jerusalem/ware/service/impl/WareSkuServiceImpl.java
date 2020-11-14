package com.jerusalem.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.jerusalem.common.enume.OrderStatusEnum;
import com.jerusalem.common.exception.NoStockException;
import com.jerusalem.common.to.StockDetailTo;
import com.jerusalem.common.to.StockLockedTo;
import com.jerusalem.common.utils.R;
import com.jerusalem.common.vo.LockStockVo;
import com.jerusalem.common.vo.OrderItemVo;
import com.jerusalem.goods.feign.SpuInfoFeign;
import com.jerusalem.common.vo.SkuStockVo;
import com.jerusalem.order.entity.OrdersEntity;
import com.jerusalem.order.feign.OrdersFeign;
import com.jerusalem.ware.entity.WareOrderTaskDetailEntity;
import com.jerusalem.ware.entity.WareOrderTaskEntity;
import com.jerusalem.ware.service.WareOrderTaskDetailService;
import com.jerusalem.ware.service.WareOrderTaskService;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;
import com.jerusalem.ware.dao.WareSkuDao;
import com.jerusalem.ware.entity.WareSkuEntity;
import com.jerusalem.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * 商品库存
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;

    @Autowired
    private SpuInfoFeign spuInfoFeign;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    OrdersFeign ordersFeign;

    /**
    * 根据仓库、SKU ID进行分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }
        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    /***
     * 商品的入库
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    @Transactional
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断,若没有这个库存记录，则新增
        List<WareSkuEntity> wareSkuEntityList = wareSkuDao.selectList(
                new QueryWrapper<WareSkuEntity>()
                        .eq("sku_id", skuId)
                        .eq("ware_id", wareId));
        if(wareSkuEntityList == null || wareSkuEntityList.size() == 0){
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            //catch异常,远程查询sku的名字，如果失败，整个事务无需回滚
            try {
                R info = spuInfoFeign.info(skuId);
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                if(info.getCode() == 0){
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){
            }
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            //新增一条库存记录
            wareSkuDao.insert(skuEntity);
        }else{
            //自定义的入库操作（添加库存）
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }
    }

    /***
     * 查询Sku是否有库存
     * @param skuIds
     * @return
     */
    @Override
    public List<SkuStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuStockVo> collect = skuIds.stream().map(skuId -> {
            SkuStockVo skuStockVo = new SkuStockVo();
            //查询当前sku的总库存量
            Long count = baseMapper.getSkuStock(skuId);
            skuStockVo.setSkuId(skuId);
            skuStockVo.setHasStock(count == null ? false : count > 0);
            return skuStockVo;
        }).collect(Collectors.toList());
        return collect;
    }

    /***
     * 锁定库存
     * 事务机制：必须所有商品的所有库存锁定成功，否则回滚
     * rollbackFor = NoStockException.class  表示遇到NoStockException.class回滚
     * 默认也是回滚（遇到运行时异常都回滚）
     * @param lockStockVo
     * @return
     */
    @Transactional(rollbackFor = NoStockException.class)
    @Override
    public Boolean lockStock(LockStockVo lockStockVo) {
        /**
         * 首先保存库存工作单 -》 追溯
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(lockStockVo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);
        //1.查出所有商品有库存的仓库的ID
        List<OrderItemVo> orderItemLocks = lockStockVo.getOrderItemLocks();
        List<SkuHasStockWare> hasStockWares = orderItemLocks.stream().map(item -> {
            SkuHasStockWare skuHasStockWare = new SkuHasStockWare();
            Long skuId = item.getSkuId();
            skuHasStockWare.setSkuId(skuId);
            List<Long> wareIds = baseMapper.SkuHasStockWareId(skuId);
            skuHasStockWare.setWareIds(wareIds);
            Integer count = item.getCount();
            skuHasStockWare.setNum(count);
            return skuHasStockWare;
        }).collect(Collectors.toList());

        //2.锁定库存
        for (SkuHasStockWare hasStockWare : hasStockWares) {
            Boolean skuStocked = false; //感知当前商品的库存是否锁定成功
            Long skuId = hasStockWare.getSkuId();
            Integer num = hasStockWare.getNum();
            List<Long> wareIds = hasStockWare.getWareIds();
            if (wareIds == null || wareIds.size() == 0){
                //无仓库有该商品的库存
                throw new NoStockException(skuId);
            }
            //若每一个商品锁定成功，将当前商品锁定了几件的工作单详情记录发给mq
            //若锁定失败，前面保存的工作单详情将回滚
            for (Long wareId : wareIds) {
                //返回数据库表影响行数 1-成功 0-失败
                Long count = wareSkuDao.lockStock(skuId,wareId,num);
                if (count == 1){
                    //锁定库存成功，更改锁住状态，保存库存工作单详情，发送消息给mq，退出循环，进行下一个商品
                    skuStocked = true;
                    WareOrderTaskDetailEntity wareOrderTaskDetailEntity =
                            new WareOrderTaskDetailEntity(null,skuId,"",hasStockWare.getNum(),wareOrderTaskEntity.getId(),wareId,1);
                    wareOrderTaskDetailService.save(wareOrderTaskDetailEntity);
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setTaskId(wareOrderTaskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(wareOrderTaskDetailEntity,stockDetailTo);
                    stockLockedTo.setStockDetailTo(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",stockLockedTo);
                    break;
                }else {
                    //当前仓库锁定库存失败，继续下一个仓库
                }
            }
            if (skuStocked == false){
                //当前商品库存锁定失败，即该商品所有仓库的库存都不足
                throw new NoStockException(skuId);
            }
        }
        //能进行到此处，一定是所有商品都锁定成功,返回true
        return true;
    }

    /***
     * 解锁库存
     * @param stockLockedTo
     */
    @Override
    public void unLockStock(StockLockedTo stockLockedTo) {
        StockDetailTo stockDetailTo = stockLockedTo.getStockDetailTo();//拿到要解锁的库存工作单的详情
        Long detailId = stockDetailTo.getId();
        WareOrderTaskDetailEntity detailEntity = wareOrderTaskDetailService.getById(detailId);
        if (detailEntity!=null){
            //有库存工作单详情，证明库存业务没问题，需要进行解锁
            Long taskId = stockLockedTo.getTaskId();//拿到要解锁的库存工作单的id
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(taskId);
            String orderSn = taskEntity.getOrderSn();//根据订单号查询订单状态
            R r = ordersFeign.getOrderStatus(orderSn);
            if (r.getCode() == 0){
                //订单数据返回成功
                OrdersEntity ordersEntity = r.getData(new TypeReference<OrdersEntity>() {
                });
                Integer status = ordersEntity.getStatus();
                //订单不存在或订单的状态为已取消，解锁
                if (ordersEntity == null ||status == OrderStatusEnum.CANCLED.getCode()){
                    baseMapper.unLockStock(detailEntity.getSkuId(),detailEntity.getWareId(),detailEntity.getSkuNum());
                    if (detailEntity.getLockStatus() == 1){
                        WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity();
                        entity.setId(detailId);
                        entity.setLockStatus(2);
                        wareOrderTaskDetailService.updateById(entity);
                    }
                }
            }else {
                //远程调用失败，抛异常
                throw new RuntimeException("远程服务调用失败");
            }
        }else {
            //库存工作单详情不存在，库存本身锁定失败，自己回滚，无需解锁
        }
    }

    /***
     * 内部类
     * 某商品有库存的仓库的id集合
     */
    @Data
    class SkuHasStockWare{
        private Long skuId;
        private Integer num;
        private List<Long> wareIds;
    }
}