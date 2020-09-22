package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.goods.entity.SkuImagesEntity;

import java.util.List;

/****
 * 服务层接口
 * sku图片
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    /***
     * 根据SkuID获取图片集合
     * @param skuId
     * @return
     */
    List<SkuImagesEntity> getBySkuId(Long skuId);
}

