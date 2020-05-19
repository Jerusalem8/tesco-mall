package com.jerusalem.ware.dao;

import com.jerusalem.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
    long getSkuStock(Long skuId);
}
