package com.jerusalem.goods.vo;

import lombok.Data;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SpuBaseAttrGroupVo
 * @Date 2020/9/20 16:11
 *****/
@Data
public class SpuBaseAttrGroupVo {
    private String groupName;
    private List<Attr> attrs;
}
