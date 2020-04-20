package com.jerusalem.user.dao;

import com.jerusalem.user.entity.UsersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 用户
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Mapper
public interface UsersDao extends BaseMapper<UsersEntity> {
	
}
