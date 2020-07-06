package com.jerusalem.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.jerusalem.common.esTo.SkuEsModel;
import com.jerusalem.search.config.EsConfig;
import com.jerusalem.search.constant.EsConstant;
import com.jerusalem.search.service.SearchService;
import com.jerusalem.search.vo.SearchParam;
import com.jerusalem.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/****
 * @Author: jerusalem
 * @Description: SearchService
 * @Date 2020/6/25 14:49
 *****/
@Service("searchService")
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /***
     * 检索方法
     * 根据页面传递来查询参数，去ES查询，封装到SearchResult返回给页面
     * @param searchParam
     * @return
     */
    @Override
    public SearchResult search(SearchParam searchParam) {
        SearchResult searchResult = null;
        //1.构建检索请求
        SearchRequest searchRequest = searchRequestBuilder(searchParam);
        try {
            //2.执行检索请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, EsConfig.COMMON_OPTIONS);
            //3.分析响应数据，封装成需要的格式
            searchResult = searchResponseBuilder(searchResponse,searchParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }


    /***
     * 抽取的方法
     * 检索请求构造器 -> SearchRequest
     * @return
     */
    private SearchRequest searchRequestBuilder(SearchParam searchParam) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        /**
         * 构建 DSL检索语句
         * 1.构建boolQuery -> 模糊查询 过滤
         */
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //1.1 must-模糊匹配
        if (!StringUtils.isEmpty(searchParam.getKeyword())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle",searchParam.getKeyword()));
        }
        //1.2.1 filter-三级分类ID
        if (searchParam.getCategory3Id() != null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryId",searchParam.getCategory3Id()));
        }
        //1.2.2 filter-品牌ID
        if (searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",searchParam.getBrandId()));
        }
        //1.2.3 filter-属性
        if (searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0){
            for (String attrStr : searchParam.getAttrs()) {
                BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                //attr=1_黑色:白色%attr=2_5存:6存
                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestedBoolQueryBuilder.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestedBoolQueryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue",attrValues));
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", nestedBoolQueryBuilder, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            }
        }
        //1.2.4 filter-是否有库存
        if (searchParam.getHasStock() != null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock",searchParam.getHasStock() == 1));
        }
        //1.2.5 filter-价格区间
        if(!StringUtils.isEmpty(searchParam.getSkuPrice())){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            //skuPrice=500以下，500-1000，1000以上（三种情况）
            String[] s = searchParam.getSkuPrice().split("_");
            if (s.length == 2){
                rangeQueryBuilder.gte(s[0]).lte(s[1]);
            }else if (s.length == 1){
                if (searchParam.getSkuPrice().startsWith("_")){
                    rangeQueryBuilder.lte(s[0]);
                }
                if (searchParam.getSkuPrice().endsWith("_")){
                    rangeQueryBuilder.gte(s[0]);
                }
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);

        /**
         * 构建 DSL检索语句
         * 2.排序 分页 高亮
         */
        //2.1 排序
        if (!StringUtils.isEmpty(searchParam.getSort())){
            //sort=hasStock_asc/desc
            String sort = searchParam.getSort();
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc")?SortOrder.ASC:SortOrder.DESC;
            searchSourceBuilder.sort(s[0], order);
        }
        //2.2 分页
        searchSourceBuilder.from((searchParam.getPageNum()-1)*EsConstant.PRODUCT_PAGESIZE);
        searchSourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        //2.3 高亮
        if (!StringUtils.isEmpty(searchParam.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style=color:red>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        /**
         * 构建 DSL检索语句
         * 3.聚合分析
         */
        //3.1 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        //3.1.1 品牌聚合子聚合-品牌名
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        //3.1.2 品牌聚合子聚合-品牌logo
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        searchSourceBuilder.aggregation(brand_agg);

        //3.2 分类聚合
        TermsAggregationBuilder category_agg = AggregationBuilders.terms("category_agg");
        category_agg.field("categoryId").size(20);
        //3.2.1 分类聚合子聚合-分类名
        category_agg.subAggregation(AggregationBuilders.terms("category_name_agg").field("categoryName").size(1));
        searchSourceBuilder.aggregation(category_agg);

        //3.3 属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        //3.3.1 属性聚合子聚合-属性ID
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg");
        attr_id_agg.field("attrs.attrId").size(1);
        //3.3.1.1 属性聚合子聚合-属性ID的子聚合-属性名
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        //3.3.1.2 属性聚合子聚合-属性ID的子聚合-属性值
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(20));
        attr_agg.subAggregation(attr_id_agg);
        searchSourceBuilder.aggregation(attr_agg);

        //执行最终的检索请求
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);
        return searchRequest;
    }

    /***
     * 抽取的方法
     * 分析响应数据，封装成需要的格式 SearchResult
     * @param searchResponse
     * @return
     */
    private SearchResult searchResponseBuilder(SearchResponse searchResponse,SearchParam searchParam) {

        SearchResult searchResult = new SearchResult();

        /**
         * 封装返回信息SearchResult
         * 1.所有商品信息
         */
        SearchHits hits = searchResponse.getHits();
        List<SkuEsModel> skuEsModelList = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0){
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                //设置高亮
                if (!StringUtils.isEmpty(searchParam.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String highlightTitleString = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(highlightTitleString);
                }
                skuEsModelList.add(skuEsModel);
            }
        }
        searchResult.setProducts(skuEsModelList);

        /**
         * 封装返回信息SearchResult
         * 2.所有商品涉及到的分类聚合信息
         */
        ParsedLongTerms category_agg = searchResponse.getAggregations().get("category_agg");
        List<SearchResult.CategoryVo> categoryVoList = new ArrayList<>();
        List<? extends Terms.Bucket> categoryAggBuckets = category_agg.getBuckets();
        for (Terms.Bucket categoryAggBucket : categoryAggBuckets) {
            SearchResult.CategoryVo categoryVo = new SearchResult.CategoryVo();
            //得到分类ID
            String categoryKey = categoryAggBucket.getKeyAsString();
            categoryVo.setCategoryId(Long.parseLong(categoryKey));
            //得到分类名称
            ParsedStringTerms category_name_agg = categoryAggBucket.getAggregations().get("category_name_agg");
            String categoryName = category_name_agg.getBuckets().get(0).getKeyAsString();
            categoryVo.setCategoryName(categoryName);
            //封装结果添加进集合
            categoryVoList.add(categoryVo);
        }
        searchResult.setCategorys(categoryVoList);

        /**
         * 封装返回信息SearchResult
         * 2.所有商品涉及到的品牌聚合信息
         */
        ParsedLongTerms brand_agg = searchResponse.getAggregations().get("brand_agg");
        List<SearchResult.BrandVo> brandVoList = new ArrayList<>();
        List<? extends Terms.Bucket> brandAggBuckets = brand_agg.getBuckets();
        for (Terms.Bucket brandAggBucket : brandAggBuckets) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //得到品牌ID
            long brandId = brandAggBucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brandId);
            //得到品牌名称
            ParsedStringTerms brand_name_agg = brandAggBucket.getAggregations().get("brand_name_agg");
            String brandName = brand_name_agg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brandName);
            //得到品牌logo
            ParsedStringTerms brand_img_agg = brandAggBucket.getAggregations().get("brand_img_agg");
            String brandImg = brand_img_agg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brandImg);
            //封装结果添加进集合
            brandVoList.add(brandVo);
        }
        searchResult.setBrands(brandVoList);

        /**
         * 封装返回信息SearchResult
         * 3.所有商品涉及到的属性聚合信息
         */
        ParsedNested attr_agg = searchResponse.getAggregations().get("attr_agg");
        List<SearchResult.AttrVo> attrVoList = new ArrayList<>();
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        List<? extends Terms.Bucket> attrIdAggBuckets = attr_id_agg.getBuckets();
        for (Terms.Bucket attrIdAggBucket : attrIdAggBuckets) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //得到属性ID
            long attrId = attrIdAggBucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);
            //得到属性名称
            ParsedStringTerms attr_name_agg = attrIdAggBucket.getAggregations().get("attr_name_agg");
            String attrName = attr_name_agg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);
            //得到属性值
            ParsedStringTerms attr_value_agg = attrIdAggBucket.getAggregations().get("attr_value_agg");
            List<String> attrValueList = attr_value_agg.getBuckets().stream().map(item -> {
                String attrValue = item.getKeyAsString();
                return attrValue;
            }).collect(Collectors.toList());
            attrVo.setAttrValue(attrValueList);
            //封装结果添加进集合
            attrVoList.add(attrVo);
        }
        searchResult.setAttrs(attrVoList);

        /**
         * 分页信息
         * 当前页，总记录数，总页数
         */
        searchResult.setPageNum(searchParam.getPageNum());
        long total = hits.getTotalHits().value;
        searchResult.setTotal(total);
        int totalPages = (int)total%EsConstant.PRODUCT_PAGESIZE == 0 ? (int)total/EsConstant.PRODUCT_PAGESIZE : ((int)total/EsConstant.PRODUCT_PAGESIZE+1);
        searchResult.setTotalPages(totalPages);

        //完成封装返回
        return searchResult;
    }
}
