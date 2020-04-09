package com.jerusalem.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.ware.entity.PurchaseEntity;

import java.util.Map;

/****
 * 服务层接口
 * 采购信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

