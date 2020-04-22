package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/****
 * 服务层接口
 * spu图片
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 保存商品的图片集
     * @param id
     * @param images
     */
    void saveImages(Long id, List<String> images);
}

