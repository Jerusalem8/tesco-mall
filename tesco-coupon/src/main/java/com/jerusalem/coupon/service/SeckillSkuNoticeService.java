package com.jerusalem.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/****
 * 服务层接口
 * 秒杀商品通知订阅
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:47:38
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

