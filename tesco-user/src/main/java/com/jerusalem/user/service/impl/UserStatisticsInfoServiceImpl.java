package com.jerusalem.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.user.dao.UserStatisticsInfoDao;
import com.jerusalem.user.entity.UserStatisticsInfoEntity;
import com.jerusalem.user.service.UserStatisticsInfoService;

/****
 * 服务层接口实现类
 * 用户统计信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Service("userStatisticsInfoService")
public class UserStatisticsInfoServiceImpl extends ServiceImpl<UserStatisticsInfoDao, UserStatisticsInfoEntity> implements UserStatisticsInfoService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserStatisticsInfoEntity> page = this.page(
                new Query<UserStatisticsInfoEntity>().getPage(params),
                new QueryWrapper<UserStatisticsInfoEntity>()
        );
        return new PageUtils(page);
    }

}