package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.AttrGroupEntity;
import com.jerusalem.goods.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/****
 * 服务层接口
 * 属性分组
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    /**
     * 根据三级分类ID、搜索关键词（属性分组名）查询属性分组
     * @param params
     * @param categoryId
     * @return
     */
    PageUtils queryPage(Map<String, Object> params, Long categoryId);

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 根据三级分类ID查询属性分组及分组下的所有属性
     * @param categoryId
     * @return
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long categoryId);
}

