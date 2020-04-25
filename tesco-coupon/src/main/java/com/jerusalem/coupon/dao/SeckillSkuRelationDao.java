package com.jerusalem.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerusalem.coupon.entity.SeckillSkuRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/****
 * 持久层
 * 秒杀活动商品关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Mapper
@Repository
public interface SeckillSkuRelationDao extends BaseMapper<SeckillSkuRelationEntity> {
	
}
