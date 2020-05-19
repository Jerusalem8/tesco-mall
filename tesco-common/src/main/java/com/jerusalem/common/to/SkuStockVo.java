package com.jerusalem.common.to;

import lombok.Data;

/****
 * 视图对象
 * 1.接收页面传递的数据，封装对象
 * 2.将业务处理后的对象，封装成页面所需的数据
 *
 * SkuStockVo：库存
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-05-18 14:48:19
 */
@Data
public class SkuStockVo {
    private Long skuId;
    private Boolean hasStock;
}
