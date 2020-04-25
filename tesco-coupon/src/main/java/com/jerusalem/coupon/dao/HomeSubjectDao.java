package com.jerusalem.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerusalem.coupon.entity.HomeSubjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/****
 * 持久层
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Mapper
@Repository
public interface HomeSubjectDao extends BaseMapper<HomeSubjectEntity> {
	
}
