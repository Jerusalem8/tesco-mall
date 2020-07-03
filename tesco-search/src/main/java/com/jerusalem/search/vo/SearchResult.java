package com.jerusalem.search.vo;

import com.jerusalem.common.esTo.SkuEsModel;
import lombok.Data;

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
     */
    private Integer pageNum;
    private Long total;
    private Integer totalPages;

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
     * 查询结果涉及到的品牌集合
     */
    private List<attrVo> attrs;

    @Data
    public static class attrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    /**
     * 查询结果涉及到的分类集合
     */
    private List<categoryVo> categorys;

    @Data
    public static class categoryVo{
        private Long categoryId;
        private String categoryName;
    }
}
