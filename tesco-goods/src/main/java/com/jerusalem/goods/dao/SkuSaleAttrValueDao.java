package com.jerusalem.goods.dao;

import com.jerusalem.goods.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerusalem.goods.vo.SkuSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/****
 * 持久层
 * sku销售属性&值
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Mapper
@Repository
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    /***
     * 根据SPUID获取销售属性集合
     * @param spuId
     * @return
     */
    List<SkuSaleAttrVo> getSaleAttrVoBySpuId(@Param("spuId")Long spuId);
}
