package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.AttrEntity;
import com.jerusalem.goods.vo.AttrResponseVo;
import com.jerusalem.goods.vo.AttrVo;
import java.util.Map;

/****
 * 服务层接口
 * 商品属性
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface AttrService extends IService<AttrEntity> {

    /***
     * 根据分类ID、关键词分页查询基本属性
     * @param params
     * @param categoryId
     * @param attrType
     * @return
     */
    PageUtils queryAttrPage(Map<String, Object> params, Long categoryId, String attrType);

    /***
     * 查询属性的信息（实现修改时数据的回显）
     * @param attrId
     * @return
     */
    AttrResponseVo getAttrInfo(Long attrId);

    /***
     * 新增属性
     * @param attr
     */
    void saveAttr(AttrVo attr);

    /***
     * 修改属性的信息
     * @param attr
     */
    void updateAttr(AttrVo attr);
}

