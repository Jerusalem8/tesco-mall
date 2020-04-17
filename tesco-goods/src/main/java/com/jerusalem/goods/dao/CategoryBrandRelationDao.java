package com.jerusalem.goods.dao;

import com.jerusalem.goods.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/****
 * 持久层
 * 品牌分类关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Mapper
@Repository
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

//    /***
//     * 级联更新分类
//     * @param categoryId
//     * @param name
//     */
//    void updateCategory(@Param("categoryId") Long categoryId, @Param("name") String name);
}
