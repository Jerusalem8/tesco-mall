package com.jerusalem.goods.dao;

import com.jerusalem.goods.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/****
 * 持久层
 * 商品属性
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Mapper
@Repository
public interface AttrDao extends BaseMapper<AttrEntity> {

    /***
     * 在指定的属性ID集合中查出可检索的属性ID集合
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(@Param("attrIds") List<Long> attrIds);
}
