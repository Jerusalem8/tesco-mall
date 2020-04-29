package com.jerusalem.ware.dao;

import com.jerusalem.ware.entity.PurchaseDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/****
 * 持久层
 * 
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@Mapper
@Repository
public interface PurchaseDetailDao extends BaseMapper<PurchaseDetailEntity> {
	
}
