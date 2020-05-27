package com.jerusalem.search.controller;

import com.jerusalem.common.esTo.SkuEsModel;
import com.jerusalem.common.exception.BizCodeEnume;
import com.jerusalem.common.utils.R;
import com.jerusalem.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SearchController
 * @Date 2020/5/19 17:44
 *****/
@Slf4j
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
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList) {
        boolean b = false;
        try {
            b = searchService.productStatusUp(skuEsModelList);
        }catch (Exception e){
            log.error("ES商品上架错误:{}",e);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }

        if (!b){
            return R.ok();
        } else {
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
