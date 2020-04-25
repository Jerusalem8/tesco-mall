package com.jerusalem.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerusalem.coupon.entity.HomeSubjectSpuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/****
 * 持久层
 * 专题商品
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Mapper
@Repository
public interface HomeSubjectSpuDao extends BaseMapper<HomeSubjectSpuEntity> {
	
}
