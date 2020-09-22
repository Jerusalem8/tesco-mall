package com.jerusalem.goods.dao;

import com.jerusalem.goods.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerusalem.goods.vo.SkuItemVo;
import com.jerusalem.goods.vo.SpuBaseAttrGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/****
 * 持久层
 * 属性分组
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Mapper
@Repository
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    /***
     * 根据三级分类ID，SPUID查询属性分组以及其属性对应的值
     * @param spuId
     * @param categoryId
     * @return
     */
    List<SpuBaseAttrGroupVo> getAttrGroupVos(@Param("spuId") Long spuId, @Param("categoryId") Long categoryId);

}
