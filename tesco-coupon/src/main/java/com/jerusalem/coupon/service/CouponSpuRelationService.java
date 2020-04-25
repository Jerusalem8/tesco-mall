package com.jerusalem.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.coupon.entity.CouponSpuRelationEntity;

import java.util.Map;
/****
 * 服务层接口
 * 优惠券与产品关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
public interface CouponSpuRelationService extends IService<CouponSpuRelationEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

