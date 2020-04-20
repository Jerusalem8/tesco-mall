package com.jerusalem.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.user.dao.UserCollectSpuDao;
import com.jerusalem.user.entity.UserCollectSpuEntity;
import com.jerusalem.user.service.UserCollectSpuService;

/****
 * 服务层接口实现类
 * 用户收藏的商品
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Service("userCollectSpuService")
public class UserCollectSpuServiceImpl extends ServiceImpl<UserCollectSpuDao, UserCollectSpuEntity> implements UserCollectSpuService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserCollectSpuEntity> page = this.page(
                new Query<UserCollectSpuEntity>().getPage(params),
                new QueryWrapper<UserCollectSpuEntity>()
        );
        return new PageUtils(page);
    }

}