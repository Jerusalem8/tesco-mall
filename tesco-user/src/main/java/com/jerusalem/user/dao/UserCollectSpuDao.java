package com.jerusalem.user.dao;

import com.jerusalem.user.entity.UserCollectSpuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 用户收藏的商品
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Mapper
public interface UserCollectSpuDao extends BaseMapper<UserCollectSpuEntity> {
	
}
