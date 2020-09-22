package com.jerusalem.goods.controller.web;

import com.jerusalem.goods.service.SkuInfoService;
import com.jerusalem.goods.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/****
 * @Author: jerusalem
 * @Description: ItemController
 * 商品详情
 * @Date 2020/9/18 16:41
 *****/
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    /***
     * 当前SKU的商品详情
     * @param skuId
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model){
        SkuItemVo skuItemVo = skuInfoService.skuItem(skuId);
        model.addAttribute("item",skuItemVo);
        return "item";
    }
}
