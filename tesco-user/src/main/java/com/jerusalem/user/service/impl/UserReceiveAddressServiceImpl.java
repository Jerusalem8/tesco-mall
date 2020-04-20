package com.jerusalem.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.user.dao.UserReceiveAddressDao;
import com.jerusalem.user.entity.UserReceiveAddressEntity;
import com.jerusalem.user.service.UserReceiveAddressService;

/****
 * 服务层接口实现类
 * 用户收货地址
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Service("userReceiveAddressService")
public class UserReceiveAddressServiceImpl extends ServiceImpl<UserReceiveAddressDao, UserReceiveAddressEntity> implements UserReceiveAddressService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserReceiveAddressEntity> page = this.page(
                new Query<UserReceiveAddressEntity>().getPage(params),
                new QueryWrapper<UserReceiveAddressEntity>()
        );
        return new PageUtils(page);
    }

}