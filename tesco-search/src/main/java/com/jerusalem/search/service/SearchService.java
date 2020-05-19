package com.jerusalem.search.service;

import com.jerusalem.common.esTo.SkuEsModel;

import java.io.IOException;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SearchService
 * @Date 2020/5/19 17:54
 *****/
public interface SearchService {

    /***
     * 上架商品（保存到Es）
     * @param skuEsModelList
     * @return
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException;

}
