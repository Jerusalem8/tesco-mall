package com.jerusalem.coupon.service.impl;

import com.jerusalem.common.to.SkuReductionTo;
import com.jerusalem.common.to.UserPriceTo;
import com.jerusalem.coupon.entity.SkuLadderEntity;
import com.jerusalem.coupon.entity.UserPriceEntity;
import com.jerusalem.coupon.service.SkuLadderService;
import com.jerusalem.coupon.service.UserPriceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;
import com.jerusalem.coupon.dao.SkuFullReductionDao;
import com.jerusalem.coupon.entity.SkuFullReductionEntity;
import com.jerusalem.coupon.service.SkuFullReductionService;

/****
 * 服务层接口实现类
 * 商品满减信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:47:38
 */
@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private UserPriceService userPriceService;

    /**
     * 分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );
        return new PageUtils(page);
    }

    /***
     * 保存满减信息
     * @param skuReductionTo
     */
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        //sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if(skuReductionTo.getFullCount() > 0){
            skuLadderService.save(skuLadderEntity);
        }

        //sku_full_reduction
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo,reductionEntity);
        if(reductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1){
            this.save(reductionEntity);
        }

        //user_price
        List<UserPriceTo> userPrice = skuReductionTo.getUserPrices();
        List<UserPriceEntity> collect = userPrice.stream().map(item -> {
            UserPriceEntity userPriceEntity = new UserPriceEntity();
            userPriceEntity.setSkuId(skuReductionTo.getSkuId());
            userPriceEntity.setUserLevelId(item.getId());
            userPriceEntity.setUserLevelName(item.getName());
            userPriceEntity.setUserPrice(item.getPrice());
            userPriceEntity.setAddOther(1);
            return userPriceEntity;
        }).filter(item->{
            return item.getUserPrice().compareTo(new BigDecimal("0")) == 1;
        }).collect(Collectors.toList());
        userPriceService.saveBatch(collect);
    }
}