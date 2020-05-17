package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.SpuInfoEntity;

import java.util.Map;

/****
 * 服务层接口
 * spu信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    /**
    * 根据分类、品牌、状态、关键词进行分页查询
    * @param params
    * @return
    */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /***
     * 保存SPU的基本信息
     * @param spuInfo
     */
    void saveSpuInfo(SpuInfoEntity spuInfo);

    /***
     * 商品上架（发送sku数据到Es）
     * @param spuId
     * @return
     */
    void upSpu(Long spuId);
}

