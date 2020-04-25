package com.jerusalem.coupon.service.impl;

import com.jerusalem.coupon.dao.SeckillPromotionDao;
import com.jerusalem.coupon.entity.SeckillPromotionEntity;
import com.jerusalem.coupon.service.SeckillPromotionService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

/****
 * 服务层接口实现类
 * 秒杀活动
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Service("seckillPromotionService")
public class SeckillPromotionServiceImpl extends ServiceImpl<SeckillPromotionDao, SeckillPromotionEntity> implements SeckillPromotionService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillPromotionEntity> page = this.page(
                new Query<SeckillPromotionEntity>().getPage(params),
                new QueryWrapper<SeckillPromotionEntity>()
        );
        return new PageUtils(page);
    }

}