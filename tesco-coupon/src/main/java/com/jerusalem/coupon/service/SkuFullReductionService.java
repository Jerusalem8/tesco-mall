package com.jerusalem.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.to.SkuReductionTo;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/****
 * 服务层接口
 * 商品满减信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:47:38
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 保存满减信息
     * @param skuReductionTo
     */
    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

