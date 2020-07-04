package com.jerusalem.search.vo;

import lombok.Data;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SearchParam
 * 页面传递到后台的检索条件的封装
 * @Date 2020/6/25 11:54
 *****/
@Data
public class SearchParam {

    /**
     * 全文检索
     * 1.关键字（skuTitle）
     */
    private String keyword;

    /**
     * 点击三级分类树
     */
    private Long category3Id;

    /**
     * 过滤条件
     * 1.是否有货（默认有库存）
     * 2.价格区间
     * 3.品牌
     * 4.属性
     */
    private Integer hasStock = 1;
    private String skuPrice;
    private List<Long> brandId;
    private List<String> attrs;

    /**
     * 排序条件
     * 1.销量
     * 2.热度评分
     * 3.价格
     */
    private String sort;

    /**
     * 页码（默认为第一页）
     */
    private Integer pageNum = 1;

}
