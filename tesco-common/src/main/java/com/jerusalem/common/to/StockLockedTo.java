package com.jerusalem.common.to;

import lombok.Data;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: StockLockedTo
 * 用于给rabbitmq发送消息
 * @Date 2020/11/14 15:11
 *****/
@Data
public class StockLockedTo {

    /**
     * 库存工作单id
     */
    private Long taskId;

    /***
     * 工作单详情
     */
    private StockDetailTo stockDetailTo;

}
