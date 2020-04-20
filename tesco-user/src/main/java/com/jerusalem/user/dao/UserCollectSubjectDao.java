package com.jerusalem.user.dao;

import com.jerusalem.user.entity.UserCollectSubjectEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 用户收藏的专题活动
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Mapper
public interface UserCollectSubjectDao extends BaseMapper<UserCollectSubjectEntity> {
	
}
