package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.CategoryEntity;
import com.jerusalem.goods.vo.Category2Vo;
import org.springframework.cache.annotation.Cacheable;

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
     * 查询所有分类，并以树形结构组装（后台管理系统）
     * @return
     */
    List<CategoryEntity> listWithTree();

    /**
     * 逻辑删除
     * @param asList
     */
    void removeMenuIds(List<Long> asList);

    /**
     * 根据第三级分类ID查询完整的三级分类ID路径
     * @param categoryId
     * @return
     */
    Long[] findCategoryPath(Long categoryId);

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 级联更新
     * 更新分类表及其他关联表的关联数据
     * @return
     */
    void updateCascade(CategoryEntity category);

    /***
     * 获取所有的一级分类（首页）
     * @return
     */
    List<CategoryEntity> getCategoryLevelOne();

    /***
     * 获取三级分类数据树（首页）
     * 方法一：整合使用 SpringCache
     * @return
     */
    Map<String, List<Category2Vo>> getCategoryJsonWithSpringCache();

    /***
     * 获取三级分类数据树（首页）
     * 方法二：特殊处理，整合各种高级锁
     * @return
     */
    Map<String, List<Category2Vo>> getCategoryJson();
}

