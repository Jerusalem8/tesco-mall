package com.jerusalem.goods.dao;

import com.jerusalem.goods.entity.AttrAttrGroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/****
 * 持久层
 * 属性&属性分组关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Mapper
@Repository
public interface AttrAttrGroupRelationDao extends BaseMapper<AttrAttrGroupRelationEntity> {

    /***
     * 批量删除、删除关联关系
     * @param relationList
     */
    void deleteBatchRelation(@Param("relationList") List<AttrAttrGroupRelationEntity> relationList);
}
