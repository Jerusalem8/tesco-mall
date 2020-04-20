package com.jerusalem.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.user.dao.UsersDao;
import com.jerusalem.user.entity.UsersEntity;
import com.jerusalem.user.service.UsersService;

/****
 * 服务层接口实现类
 * 用户
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UsersEntity> page = this.page(
                new Query<UsersEntity>().getPage(params),
                new QueryWrapper<UsersEntity>()
        );
        return new PageUtils(page);
    }

}