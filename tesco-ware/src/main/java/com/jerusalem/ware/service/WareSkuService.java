package com.jerusalem.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.to.OrderTo;
import com.jerusalem.common.to.StockLockedTo;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.vo.LockStockVo;
import com.jerusalem.ware.entity.WareSkuEntity;
import com.jerusalem.common.vo.SkuStockVo;

import java.util.List;
import java.util.Map;

/****
 * 服务层接口
 * 商品库存
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    /**
    * 根据仓库、SKU ID进行分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 商品的入库
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /***
     * 查询Sku是否有库存
     * @param skuIds
     * @return
     */
    List<SkuStockVo> getSkuHasStock(List<Long> skuIds);

    /***
     * 锁定库存
     * @param lockStockVo
     * @return
     */
    Boolean lockStock(LockStockVo lockStockVo);

    /***
     * 解锁库存
     * @param stockLockedTo
     */
    void unLockStock(StockLockedTo stockLockedTo);

    /***
     * 重载解锁库存的方法
     * 接受订单关闭消息，解锁库存（防止网络原因导致的库存无法解锁）
     * @param orderTo
     */
    void unLockStock(OrderTo orderTo);
}

