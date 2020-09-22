package com.jerusalem.user.service.impl;

import com.jerusalem.user.dao.UserLevelDao;
import com.jerusalem.user.entity.UserLevelEntity;
import com.jerusalem.user.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserLevelDao userLevelDao;

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

    /***
     * 注册
     * @param userRegisterVo
     */
    @Override
    public void register(UserRegisterVo userRegisterVo) {
        UsersEntity usersEntity = new UsersEntity();
        //设置默认信息(默认等级)
        UserLevelEntity levelEntity = userLevelDao.getDefaultLevel();
        usersEntity.setLevelId(levelEntity.getId());
        //检查用户名和手机号是否唯一（为了让Controller感知异常，使用异常机制）
        checkEmPhoneUnique(userRegisterVo.getPhone());
        checkUsernameUnique(userRegisterVo.getUserName());

        //检查通过，保存
        usersEntity.setMobile(userRegisterVo.getPhone());
        usersEntity.setUsername(userRegisterVo.getUserName());


        baseMapper.insert(usersEntity);
    }

    @Override
    public void checkEmPhoneUnique(String phone) {

    }

    @Override
    public void checkUsernameUnique(String username) {

    }

}