package com.jerusalem.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.user.entity.GrowthChangeHistoryEntity;

import java.util.Map;

/****
 * 服务层接口
 * 成长值变化历史记录
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:48:41
 */
public interface GrowthChangeHistoryService extends IService<GrowthChangeHistoryEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

