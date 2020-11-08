package com.jerusalem.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.ware.entity.WareInfoEntity;
import com.jerusalem.ware.vo.AddressWithFareVo;

import java.util.Map;

/****
 * 服务层接口
 * 仓库信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    /**
    * 根据关键词进行分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 根据收货地址获取运费
     * @param addrId
     * @return
     */
    AddressWithFareVo getFare(Long addrId);
}

