package com.jerusalem.coupon.service.impl;

import com.jerusalem.coupon.dao.SpuBoundsDao;
import com.jerusalem.coupon.entity.SpuBoundsEntity;
import com.jerusalem.coupon.service.SpuBoundsService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

/****
 * 服务层接口实现类
 * 商品spu积分设置
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<SpuBoundsEntity>()
        );
        return new PageUtils(page);
    }

}