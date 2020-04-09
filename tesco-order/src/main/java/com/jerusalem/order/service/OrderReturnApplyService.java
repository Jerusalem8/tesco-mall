package com.jerusalem.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.order.entity.OrderReturnApplyEntity;

import java.util.Map;

/****
 * 服务层接口
 * 订单退货申请
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:49:06
 */
public interface OrderReturnApplyService extends IService<OrderReturnApplyEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

