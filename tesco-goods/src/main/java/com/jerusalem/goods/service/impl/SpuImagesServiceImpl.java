package com.jerusalem.goods.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.goods.dao.SpuImagesDao;
import com.jerusalem.goods.entity.SpuImagesEntity;
import com.jerusalem.goods.service.SpuImagesService;

/****
 * 服务层接口实现类
 * spu图片
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    /***
     * 保存SPU的图片集
     * @param id
     * @param images
     */
    @Override
    public void saveImages(Long id, List<String> images) {
        if(images == null || images.size() == 0){

        }else{
            List<SpuImagesEntity> collect = images.stream().map(img -> {
                SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                spuImagesEntity.setSpuId(id);
                spuImagesEntity.setImgUrl(img);
                return spuImagesEntity;
            }).collect(Collectors.toList());
            this.saveBatch(collect);
        }
    }

}