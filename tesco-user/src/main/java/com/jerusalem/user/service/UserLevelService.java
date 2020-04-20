package com.jerusalem.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.user.entity.UserLevelEntity;

import java.util.Map;

/****
 * 服务层接口
 * 用户等级
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
public interface UserLevelService extends IService<UserLevelEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

