package com.jerusalem.common.to;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/****
 * @Author: jerusalem
 * @Description: StockDetailTo
 * 工作单详情，发给mq
 * @Date 2020/11/14 15:20
 *****/
@Data
public class StockDetailTo {
    /**
     * id
     */
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;

    /**
     * 仓库id
     */
    private Long wareId;

    /**
     * 锁定状态
     */
    private Integer lockStatus;
}
