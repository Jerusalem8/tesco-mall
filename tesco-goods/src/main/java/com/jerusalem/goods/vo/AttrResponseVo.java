package com.jerusalem.goods.vo;

import lombok.Data;

/****
 * 视图对象
 * 1.接收页面传递的数据，封装对象
 * 2.将业务处理后的对象，封装成页面所需的数据
 *
 * AttrResponseVo：响应数据的封装
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-018 14:48:19
 */
@Data
public class AttrResponseVo extends AttrVo {

    /**
     * 所属分类名字
     */
    private String categoryName;
    /**
     * 所属分组名字
     */
    private String groupName;
    /**
     * 三积分类路径
     */
    private Long[] categoryPath;
}
