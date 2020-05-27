package com.jerusalem.goods.app;

import com.jerusalem.common.utils.R;
import com.jerusalem.goods.service.ProductService;
import com.jerusalem.goods.vo.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/****
 * @Author: jerusalem
 * @Description: ProductController
 * 商品的发布流程
 * @Date 2020/4/27 9:00
 *****/
@RestController
@RequestMapping("goods/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /***
     * 发布商品
     * @param SpuVo
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuVo SpuVo){
        productService.saveProduct(SpuVo);
        return R.ok();
    }

}
