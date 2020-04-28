package com.jerusalem.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.ware.entity.PurchaseEntity;

import java.util.List;
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
    * 根据采购单状态、关键词进行分页查询
    * @param params
    * @return
    */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /***
     * 查询未领取（还未开始执行的）的采购单
     * @param params
     * @return
     */
    PageUtils queryUnreceivePage(Map<String, Object> params);

    /***
     * 采购人员领取采购单
     * @param purchaseIds
     * @return
     */
    void received(List<Long> purchaseIds);
}

