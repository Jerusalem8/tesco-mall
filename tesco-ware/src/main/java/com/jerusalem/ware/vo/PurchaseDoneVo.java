package com.jerusalem.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/****
 * 视图对象
 * 1.接收页面传递的数据，封装对象
 * 2.将业务处理后的对象，封装成页面所需的数据
 *
 * PurchaseDoneVo：完成采购单
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-018 14:48:19
 */
@Data
public class PurchaseDoneVo {

    /***
     * 采购单ID
     */
    @NotNull
    private Long id;

    /***
     * 采购项集合
     */
    private List<PurchaseItemDoneVo> items;
}
