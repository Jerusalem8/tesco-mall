package com.jerusalem.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.user.dao.UserCollectSubjectDao;
import com.jerusalem.user.entity.UserCollectSubjectEntity;
import com.jerusalem.user.service.UserCollectSubjectService;

/****
 * 服务层接口实现类
 * 用户收藏的专题活动
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Service("userCollectSubjectService")
public class UserCollectSubjectServiceImpl extends ServiceImpl<UserCollectSubjectDao, UserCollectSubjectEntity> implements UserCollectSubjectService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserCollectSubjectEntity> page = this.page(
                new Query<UserCollectSubjectEntity>().getPage(params),
                new QueryWrapper<UserCollectSubjectEntity>()
        );
        return new PageUtils(page);
    }

}