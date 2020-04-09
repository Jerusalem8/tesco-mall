package com.jerusalem.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.order.entity.OrderSettingEntity;

import java.util.Map;

/****
 * 服务层接口
 * 订单配置信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:49:06
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

