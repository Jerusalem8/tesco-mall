package com.jerusalem.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.user.entity.UserReceiveAddressEntity;

import java.util.List;
import java.util.Map;

/****
 * 服务层接口
 * 用户收货地址
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
public interface UserReceiveAddressService extends IService<UserReceiveAddressEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 根据ID查询用户地址列表信息
     * @param userId
     * @return
     */
    List<UserReceiveAddressEntity> getAddressList(Long userId);
}

