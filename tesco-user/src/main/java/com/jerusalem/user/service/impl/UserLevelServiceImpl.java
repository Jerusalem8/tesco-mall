package com.jerusalem.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.user.dao.UserLevelDao;
import com.jerusalem.user.entity.UserLevelEntity;
import com.jerusalem.user.service.UserLevelService;

/****
 * 服务层接口实现类
 * 用户等级
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Service("userLevelService")
public class UserLevelServiceImpl extends ServiceImpl<UserLevelDao, UserLevelEntity> implements UserLevelService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserLevelEntity> page = this.page(
                new Query<UserLevelEntity>().getPage(params),
                new QueryWrapper<UserLevelEntity>()
        );
        return new PageUtils(page);
    }

}