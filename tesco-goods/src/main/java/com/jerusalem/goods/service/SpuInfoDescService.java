package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.goods.entity.SpuInfoDescEntity;

/****
 * 服务层接口
 * spu信息介绍
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    /***
     * 保存SPU描述图片
     * @param spuInfoDesc
     */
    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDesc);

}

