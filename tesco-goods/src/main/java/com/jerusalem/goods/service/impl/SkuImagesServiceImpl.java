package com.jerusalem.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.goods.dao.SkuImagesDao;
import com.jerusalem.goods.entity.SkuImagesEntity;
import com.jerusalem.goods.service.SkuImagesService;

import java.util.List;

/****
 * 服务层接口实现类
 * sku图片
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    /***
     * 根据skuId获取图片集
     * @param skuId
     * @return
     */
    @Override
    public List<SkuImagesEntity> getBySkuId(Long skuId) {
        return baseMapper.selectList(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId));
    }
}