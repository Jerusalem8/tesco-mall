package com.jerusalem.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.goods.entity.SpuInfoEntity;
import com.jerusalem.goods.vo.SpuVo;

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
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 商品的新增
     * @param spuVo
     */
    void saveSpu(SpuVo spuVo);

    /***
     * 保存商品的基本信息
     * @param spuInfo
     */
    void saveSpuInfo(SpuInfoEntity spuInfo);
}

