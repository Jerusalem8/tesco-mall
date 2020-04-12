package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/****
 * 服务层接口
 * 商品三级分类
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface CategoryService extends IService<CategoryEntity> {

    /***
     * 查询所有分类，并以树形结构组装
     * @return
     */
    List<CategoryEntity> listWithTree();

    /**
     * 逻辑删除
     * @param asList
     */
    void removeMenuIds(List<Long> asList);

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

