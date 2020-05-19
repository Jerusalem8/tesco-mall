package com.jerusalem.search.controller;

import com.jerusalem.common.esTo.SkuEsModel;
import com.jerusalem.common.utils.R;
import com.jerusalem.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SearchController
 * @Date 2020/5/19 17:44
 *****/
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /***
     * 上架商品（保存到Es）
     * @param skuEsModelList
     * @return
     */
    @PostMapping("/product/up")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList) throws IOException {
        try {
            searchService.productStatusUp(skuEsModelList);
        }catch (Exception e){
            return R.error();
        }
        return R.ok();
    }
}
