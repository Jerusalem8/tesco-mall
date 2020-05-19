package com.jerusalem.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.jerusalem.common.esTo.SkuEsModel;
import com.jerusalem.search.config.EsConfig;
import com.jerusalem.search.constant.EsConstant;
import com.jerusalem.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/****
 * @Author: jerusalem
 * @Description: SearchServiceImpl
 * @Date 2020/5/19 17:54
 *****/
@Slf4j
@Service("searchService")
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /***
     * 上架商品（保存到Es）
     * @param skuEsModelList
     * @return
     */
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException {
        //1.在Es中建立一个索引，并创建映射关系
        //2.在Es中保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModelList) {
            //构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, EsConfig.COMMON_OPTIONS);
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.error("商品上架错误：{}",collect);
        return b;
    }
}
