package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.goods.entity.ProductAttrValueEntity;
import java.util.List;

/****
 * 服务层接口
 * spu属性值
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {


    /***
     * 保存商品的属性及其可选值
     * @param attrValueList
     */
    void saveAttrValue(List<ProductAttrValueEntity> attrValueList);

    /***
     * 查询spu的规格信息
     * @param spuId
     * @return
     */
    List<ProductAttrValueEntity> baseAttrList(Long spuId);

    /***
     * 修改spu的规格信息
     * @param spuId
     * @param spuAttrList
     * @return
     */
    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> spuAttrList);
}

