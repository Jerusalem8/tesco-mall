package com.jerusalem.goods.service.impl;

import com.jerusalem.common.to.SkuReductionTo;
import com.jerusalem.common.to.SpuBoundsTo;
import com.jerusalem.common.utils.R;
import com.jerusalem.coupon.feign.SkuFullReductionFeign;
import com.jerusalem.coupon.feign.SpuBoundsFeign;
import com.jerusalem.goods.entity.*;
import com.jerusalem.goods.service.*;
import com.jerusalem.goods.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.goods.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * spu信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService imagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SpuBoundsFeign spuBoundsFeign;

    @Autowired
    private SkuFullReductionFeign skuFullReductionFeign;

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );
        return new PageUtils(page);
    }

    /***
     * 实现商品的新增
     * @param spuVo
     */
    @Transactional
    @Override
    public void saveSpu(SpuVo spuVo) {
        //1、保存SPU基本信息 tesco-goods ---> spu_info
        SpuInfoEntity spuInfo = new SpuInfoEntity();
        BeanUtils.copyProperties(spuVo,spuInfo);
        spuInfo.setCreateTime(new Date());
        spuInfo.setUpdateTime(new Date());
        this.saveSpuInfo(spuInfo);

        //2、保存SPU的描述图片 tesco-goods ---> spu_info_desc
        List<String> description = spuVo.getDescription();
        SpuInfoDescEntity spuInfoDesc = new SpuInfoDescEntity();
        spuInfoDesc.setSpuId(spuInfo.getId());
        spuInfoDesc.setDescription(String.join(",",description));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDesc);

        //3、保存SPU的图片集 tesco-goods ---> spu_images
        List<String> images = spuVo.getImages();
        imagesService.saveImages(spuInfo.getId(),images);

        //4、保存SPU的规格参数 tesco-goods ---> product_attr_value
        List<BaseAttrs> baseAttrs = spuVo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity attrValue = new ProductAttrValueEntity();
            attrValue.setAttrId(attr.getAttrId());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            attrValue.setAttrName(attrEntity.getAttrName());
            attrValue.setAttrValue(attr.getAttrValues());
            attrValue.setQuickShow(attr.getShowDesc());
            attrValue.setSpuId(spuInfo.getId());
            return attrValue;
        }).collect(Collectors.toList());
        attrValueService.saveAttrValue(collect);

        //5、保存spu的积分信息 tesco-sale ---> spu_bounds
        Bounds bounds = spuVo.getBounds();
        SpuBoundsTo spuBoundTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfo.getId());
        R result = spuBoundsFeign.save(spuBoundTo);
        if(result.getCode() != 0){
            log.error("远程保存SPU积分信息失败");
        }

        //6、保存当前SPU对应的所有SKU信息
        List<SkuVo> skuList = spuVo.getSkus();
        if(skuList != null && skuList.size() > 0){
            skuList.forEach(item->{
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if(image.getDefaultImg() == 1){
                        defaultImg = image.getImgUrl();
                    }
                }
                //6.1）、SKU的基本信息 tesco-goods ---> sku_info
                SkuInfoEntity skuInfo = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfo);
                skuInfo.setBrandId(spuInfo.getBrandId());
                skuInfo.setCategoryId(spuInfo.getCategoryId());
                skuInfo.setSaleCount(0L);
                skuInfo.setSpuId(spuInfo.getId());
                skuInfo.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfo);
                //拿到自增主键
                Long skuId = skuInfo.getSkuId();

                //6.2）、SKU的图片信息 tesco-goods ---> sku_images
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImages = new SkuImagesEntity();
                    skuImages.setSkuId(skuId);
                    skuImages.setImgUrl(img.getImgUrl());
                    skuImages.setDefaultImg(img.getDefaultImg());
                    return skuImages;
                }).filter(entity->{
                    //返回true就是需要，false就是剔除
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities);
                //TODO 没有图片路径的无需保存

                //6.3）、SKU的销售属性信息 tesco-goods ---> sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(saleAttr -> {
                    SkuSaleAttrValueEntity saleAttrValue = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(saleAttr, saleAttrValue);
                    saleAttrValue.setSkuId(skuId);
                    return saleAttrValue;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //6.4）、SKU的优惠、满减等信息 tesco-goods ---> sku_ladder|sku_full_reduction|user_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1){
                    R r1 = skuFullReductionFeign.saveSkuReduction(skuReductionTo);
                    if(r1.getCode() != 0){
                        log.error("远程保存SKU优惠信息失败");
                    }
                }
            });
        }
    }

    /***
     * 保存商品的基本信息
     * @param spuInfo
     */
    @Override
    public void saveSpuInfo(SpuInfoEntity spuInfo) {
        this.baseMapper.insert(spuInfo);
    }
}