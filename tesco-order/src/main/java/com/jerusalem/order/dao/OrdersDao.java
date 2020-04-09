package com.jerusalem.order.dao;

import com.jerusalem.order.entity.OrdersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 订单
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:49:06
 */
@Mapper
public interface OrdersDao extends BaseMapper<OrdersEntity> {
	
}
