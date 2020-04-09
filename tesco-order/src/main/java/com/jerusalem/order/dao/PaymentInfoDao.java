package com.jerusalem.order.dao;

import com.jerusalem.order.entity.PaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 支付信息表
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:49:06
 */
@Mapper
public interface PaymentInfoDao extends BaseMapper<PaymentInfoEntity> {
	
}
