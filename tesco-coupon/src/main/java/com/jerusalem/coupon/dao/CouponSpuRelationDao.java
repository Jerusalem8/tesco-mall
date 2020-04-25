package com.jerusalem.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerusalem.coupon.entity.CouponSpuRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/****
 * 持久层
 * 优惠券与产品关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Mapper
@Repository
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {
	
}
