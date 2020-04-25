package com.jerusalem.coupon.service.impl;

import com.jerusalem.coupon.dao.UserPriceDao;
import com.jerusalem.coupon.entity.UserPriceEntity;
import com.jerusalem.coupon.service.UserPriceService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;


/****
 * 服务层接口实现类
 * 商品用户价格
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Service("userPriceService")
public class UserPriceServiceImpl extends ServiceImpl<UserPriceDao, UserPriceEntity> implements UserPriceService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserPriceEntity> page = this.page(
                new Query<UserPriceEntity>().getPage(params),
                new QueryWrapper<UserPriceEntity>()
        );
        return new PageUtils(page);
    }

}