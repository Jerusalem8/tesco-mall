package com.jerusalem.user.dao;

import com.jerusalem.user.entity.UserReceiveAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/****
 * 持久层
 * 用户收货地址
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Mapper
@Repository
public interface UserReceiveAddressDao extends BaseMapper<UserReceiveAddressEntity> {
	
}
