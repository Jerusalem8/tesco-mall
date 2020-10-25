package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.goods.entity.SkuSaleAttrValueEntity;
import com.jerusalem.goods.vo.SkuSaleAttrVo;

import java.util.List;

/****
 * 服务层接口
 * sku销售属性&值
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    /**
     * 获取销售属性集合
     * @param spuId
     * @return
     */
    List<SkuSaleAttrVo> getSaleAttrVo(Long spuId);

    /***
     * 获取sku销售属性组合
     * @param skuId
     * @return
     */
    List<String> getSkuSaleAttrValuesAsStringList(Long skuId);
}

