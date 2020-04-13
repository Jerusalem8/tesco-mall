package com.jerusalem.user.dao;

import com.jerusalem.user.entity.MemberReceiveAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 会员收货地址
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:48:41
 */
@Mapper
public interface MemberReceiveAddressDao extends BaseMapper<MemberReceiveAddressEntity> {
	
}