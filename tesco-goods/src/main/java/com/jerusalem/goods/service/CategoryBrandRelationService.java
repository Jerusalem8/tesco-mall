package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.CategoryBrandRelationEntity;

import java.util.Map;

/****
 * 服务层接口
 * 品牌分类关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    /**
     * 新增品牌分类关联
     * @param categoryBrandRelation
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 根据品牌ID更新品牌名
     * @param brandId
     * @param name
     */
    void updateBrand(Long brandId, String name);

    /**
     * 根据分类ID更新分类名
     * @param categoryId
     * @param name
     */
    void updateCategory(Long categoryId, String name);
}

