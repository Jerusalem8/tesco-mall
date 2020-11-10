package com.jerusalem.common.vo;

import lombok.Data;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: LockStockVo
 * 锁定库存
 * @Date 2020/11/10 15:46
 *****/
@Data
public class LockStockVo {

    private String orderSn;//订单号
    private List<OrderItemVo> orderItemLocks;//需要锁住的库存信息
}
