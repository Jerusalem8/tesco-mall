package com.jerusalem.coupon.dao;

import com.jerusalem.coupon.entity.SeckillSkuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 秒杀活动商品关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:47:38
 */
@Mapper
public interface SeckillSkuRelationDao extends BaseMapper<SeckillSkuRelationEntity> {
	
}
