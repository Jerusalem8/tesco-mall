package com.jerusalem.search.vo;

import com.jerusalem.common.esTo.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SearchResponse
 * 查询结果的封装，返回给页面
 * @Date 2020/7/2 13:59
 *****/
@Data
public class SearchResult {

    /**
     * 从ES查询到的商品信息
     */
    private List<SkuEsModel> products;

    /**
     * 分页信息
     * 1.当前页
     * 2.总记录数
     * 3.总页数
     * 4.可遍历的导航页码
     */
    private Integer pageNum;
    private Long total;
    private Integer totalPages;
    private List<Integer> pageNavs;

    /**
     * 查询结果涉及到的品牌集合
     */
    private List<BrandVo> brands;

    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    /**
     * 查询结果涉及到的属性集合
     */
    private List<AttrVo> attrs;

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    /**
     * 查询结果涉及到的分类集合
     */
    private List<CategoryVo> categorys;

    @Data
    public static class CategoryVo{
        private Long categoryId;
        private String categoryName;
    }

    /**
     * 面包屑导航
     * 面包屑导航的名称、值、跳转地址
     */
    private List<NavVo> navs = new ArrayList<>();
    private List<Long> attrIds = new ArrayList<>();

    @Data
    public static class NavVo{
        private String navName;
        private String navValue;
        private String link;
    }

}
