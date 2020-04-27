package com.jerusalem.goods.service;

import com.jerusalem.goods.vo.SpuVo;

/****
 * @Author: jerusalem
 * @Description: ProductService业务层接口
 * 发布商品
 * @Date 2020/4/27 9:04
 *****/
public interface ProductService {

    /***
     * 发布商品
     * @param spuVo
     */
    void saveProduct(SpuVo spuVo);
}
