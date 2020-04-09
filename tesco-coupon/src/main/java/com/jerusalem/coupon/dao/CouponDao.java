package com.jerusalem.coupon.dao;

import com.jerusalem.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 优惠券信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:47:38
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
