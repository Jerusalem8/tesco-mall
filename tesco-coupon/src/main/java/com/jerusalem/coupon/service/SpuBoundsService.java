package com.jerusalem.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/****
 * 服务层接口
 * 商品spu积分设置
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:47:38
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 保存积分信息
     * @param spuBoundsTo
     */
    void saveSpuBounds(SpuBoundsEntity spuBoundsTo);
}

