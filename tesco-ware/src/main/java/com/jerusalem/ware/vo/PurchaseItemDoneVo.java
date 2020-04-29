package com.jerusalem.ware.vo;

import lombok.Data;

/****
 * 视图对象
 * 1.接收页面传递的数据，封装对象
 * 2.将业务处理后的对象，封装成页面所需的数据
 *
 * PurchaseItemDoneVo：完成采购项
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-018 14:48:19
 */
@Data
public class PurchaseItemDoneVo {

    /***
     * 采购项ID
     */
    private Long itemId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 采购失败的原因（可有可无）
     */
    private String reason;
}
