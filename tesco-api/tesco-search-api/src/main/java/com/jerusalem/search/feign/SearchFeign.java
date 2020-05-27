package com.jerusalem.search.feign;

import com.jerusalem.common.esTo.SkuEsModel;
import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: WareSkuFeign业务层接口
 * @Date 2020/5/19 14:26
 *****/
@FeignClient("search-service")
@RequestMapping("/search")
public interface SearchFeign {

    /***
     * 上架商品（保存到Es）
     * @param skuEsModelList
     * @return
     */
    @PostMapping("/product/up")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList);
}
