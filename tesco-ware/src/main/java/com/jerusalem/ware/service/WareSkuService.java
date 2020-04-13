package com.jerusalem.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.ware.entity.WareSkuEntity;

import java.util.Map;

/****
 * 服务层接口
 * 商品库存
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}
