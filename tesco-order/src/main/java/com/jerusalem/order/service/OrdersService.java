package com.jerusalem.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.order.entity.OrdersEntity;
import com.jerusalem.order.vo.OrderConfirmVo;

import java.util.Map;

/****
 * 服务层接口
 * 订单
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:49:06
 */
public interface OrdersService extends IService<OrdersEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 获取结算页封装信息
     * @return
     */
    OrderConfirmVo confirmOrder();
}

