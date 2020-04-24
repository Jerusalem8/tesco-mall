package com.jerusalem.coupon.service.impl;

import com.jerusalem.common.to.SkuReductionTo;
import com.jerusalem.common.to.UserPrice;
import com.jerusalem.coupon.entity.MemberPriceEntity;
import com.jerusalem.coupon.entity.SkuLadderEntity;
import org.springframework.beans.BeanUtils;
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
//        //sms_sku_ladder
//        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
//        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
//        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
//        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
//        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
//        if(skuReductionTo.getFullCount() > 0){
//            skuLadderService.save(skuLadderEntity);
//        }
//
//        //2、sms_sku_full_reduction
//        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
//        BeanUtils.copyProperties(skuReductionTo,reductionEntity);
//        if(reductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1){
//            this.save(reductionEntity);
//        }
//
//        //3、sms_member_price
//        List<UserPrice> memberPrice = skuReductionTo.getMemberPrice();
//        List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
//            MemberPriceEntity priceEntity = new MemberPriceEntity();
//            priceEntity.setSkuId(skuReductionTo.getSkuId());
//            priceEntity.setMemberLevelId(item.getId());
//            priceEntity.setMemberLevelName(item.getName());
//            priceEntity.setMemberPrice(item.getPrice());
//            priceEntity.setAddOther(1);
//            return priceEntity;
//        }).filter(item->{
//            return item.getMemberPrice().compareTo(new BigDecimal("0")) == 1;
//        }).collect(Collectors.toList());
//        memberPriceService.saveBatch(collect);
    }

}