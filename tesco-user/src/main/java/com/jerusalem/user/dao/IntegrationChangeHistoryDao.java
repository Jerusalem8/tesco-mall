package com.jerusalem.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerusalem.user.entity.IntegrationChangeHistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/****
 * 持久层
 * 积分变化历史记录
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Mapper
@Repository
public interface IntegrationChangeHistoryDao extends BaseMapper<IntegrationChangeHistoryEntity> {
	
}
