package com.jerusalem.ware.dao;

import com.jerusalem.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/****
 * 持久层
 * 商品库存
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@Mapper
@Repository
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /***
     * 入库方法
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    /***
     * 查询Sku的总库存量
     * 总库存量 = 各个仓库（库存量-锁定库存）
     * @param skuId
     * @return
     */
    Long getSkuStock(Long skuId);

    /***
     * 根据skuId查询该商品有库存的城市
     * @param skuId
     * @return
     */
    List<Long> SkuHasStockWareId(@Param("skuId")Long skuId);

    /***
     * 锁定库存
     *
     * @param skuId
     * @param wareId
     * @param num
     * @return
     */
    Long lockStock(@Param("skuId")Long skuId, @Param("wareId")Long wareId, @Param("num")Integer num);

    /****
     * 库存解锁
     * @param skuId
     * @param wareId
     * @param num
     */
    void unLockStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);
}
