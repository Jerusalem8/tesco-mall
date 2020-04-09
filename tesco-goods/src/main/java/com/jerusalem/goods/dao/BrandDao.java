package com.jerusalem.goods.dao;

import com.jerusalem.goods.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/****
 * 持久层
 * 品牌
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {
	
}
