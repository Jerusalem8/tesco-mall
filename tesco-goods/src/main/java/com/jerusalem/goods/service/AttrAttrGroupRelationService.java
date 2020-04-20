package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.AttrAttrGroupRelationEntity;
import com.jerusalem.goods.entity.AttrEntity;
import com.jerusalem.goods.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/****
 * 服务层接口
 * 属性&属性分组关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface AttrAttrGroupRelationService extends IService<AttrAttrGroupRelationEntity> {

    /***
     * 根据属性分组ID查询关联属性列表
     * @param attrGroupId
     * @return
     */
    List<AttrEntity> getRelationAttr(Long attrGroupId);

    /***
     * 删除、批量删除属性&属性分组关联关系
     * @param relationVos
     */
    void deleteRelation(AttrGroupRelationVo[] relationVos);

    /***
     * 查询未关联的属性
     * @param params
     * @param attrGroupId
     * @return
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId);

    /***
     * 新增、批量新增关联关系
     * @param relationVos
     * @return
     */
    void saveBatchRelation(List<AttrGroupRelationVo> relationVos);
}

