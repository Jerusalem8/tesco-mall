package com.jerusalem.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.coupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/****
 * 服务层接口
 * 秒杀活动商品关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

