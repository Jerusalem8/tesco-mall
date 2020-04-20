package com.jerusalem.goods.vo;

import lombok.Data;

/****
 * 视图对象
 * 1.接收页面传递的数据，封装对象
 * 2.将业务处理后的对象，封装成页面所需的数据
 *
 * AttrGroupRelationVo：属性&属性分组关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-018 14:48:19
 */
@Data
public class AttrGroupRelationVo {

    private Long attrId;
    private Long attrGroupId;
}
