package com.jerusalem.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.coupon.entity.CouponHistoryEntity;

import java.util.Map;

/****
 * 服务层接口
 * 优惠券领取历史记录
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
public interface CouponHistoryService extends IService<CouponHistoryEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

