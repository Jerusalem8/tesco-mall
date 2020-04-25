package com.jerusalem.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerusalem.coupon.entity.SpuBoundsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/****
 * 持久层
 * 商品spu积分设置
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Mapper
@Repository
public interface SpuBoundsDao extends BaseMapper<SpuBoundsEntity> {
	
}
