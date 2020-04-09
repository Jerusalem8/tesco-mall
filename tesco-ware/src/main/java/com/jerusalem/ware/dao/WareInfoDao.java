package com.jerusalem.ware.dao;

import com.jerusalem.ware.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 仓库信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
