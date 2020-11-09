package com.jerusalem.goods.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.jerusalem.common.constant.GoodsConstant;
import com.jerusalem.common.esTo.SkuEsModel;
import com.jerusalem.common.vo.SkuStockVo;
import com.jerusalem.common.utils.R;
import com.jerusalem.goods.entity.*;
import com.jerusalem.goods.service.*;
import com.jerusalem.search.feign.SearchFeign;
import com.jerusalem.ware.feign.WareSkuFeign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;
import com.jerusalem.goods.dao.SpuInfoDao;
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
    private SkuInfoService skuInfoService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private WareSkuFeign wareSkuFeign;

    @Autowired
    private SearchFeign searchFeign;


    /**
    * 根据分类、品牌、状态、关键词进行分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        //关键词
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            //因为有两个条件用or连接，所以必须使用and把条件括起来 (id=X or spu_name like xxx)
            //防止与后面拼接的eq条件发生错误
            queryWrapper.and((wrapper)->{
                wrapper.eq("id",key).or().like("spu_name",key);
            });
        }
        //状态
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            queryWrapper.eq("publish_status",status);
        }
        //品牌
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            queryWrapper.eq("brand_id",brandId);
        }
        //分类
        String categoryId = (String) params.get("categoryId");
        if(!StringUtils.isEmpty(categoryId)&&!"0".equalsIgnoreCase(categoryId)){
            queryWrapper.eq("category_id",categoryId);
        }

        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    /***
     * 保存SPU的基本信息
     * @param spuInfo
     */
    @Override
    public void saveSpuInfo(SpuInfoEntity spuInfo) {
        this.baseMapper.insert(spuInfo);
    }

    /***
     * 商品上架（发送sku数据到Es）
     * @param spuId
     * @return
     */
    @Override
    public void upSpu(Long spuId) {

        /**
         * 根据spuId查询出sku集合
         */
        List<SkuInfoEntity> skuInfoList = skuInfoService.getSkuListBySpuId(spuId);

        /**
         * 得到可检索的sku属性的ID集合
         */
        List<ProductAttrValueEntity> attrValueEntities = attrValueService.baseAttrList(spuId);
        List<Long> attrIds = attrValueEntities.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrs(attrIds);
        Set<Long> idSet = new HashSet<>(searchAttrIds);
        //过滤出可检索属性
        List<SkuEsModel.Attrs> attrsList = attrValueEntities.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());

        /**
         * 发送远程调用，查询库存系统，是否有库存
         * 若远程调用失败，返回null
         */
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        Map<Long, Boolean> stockMap = null;
        try{
            R r = wareSkuFeign.getSkuStock(skuIdList);
            TypeReference<List<SkuStockVo>> typeReference = new TypeReference<List<SkuStockVo>>(){};
            stockMap = r.getData(typeReference).stream().collect(Collectors.toMap(SkuStockVo::getSkuId, item -> item.getHasStock()));
        }catch (Exception e){
            log.error("库存服务查询异常：原因{}",e);
        }

        /**
         * 封装skuEsModel
         */
        //封装每个sku的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> skuEsModelList = skuInfoList.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            //直接对考部分属性
            BeanUtils.copyProperties(sku,skuEsModel);
            //以下属性需要特殊处理
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());

            //设置库存信息
            if (finalStockMap == null){
                skuEsModel.setHasStock(true);
            }else {
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            //TODO 热度评分
            skuEsModel.setHotScore(0L);

            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());

            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCategoryId());
            skuEsModel.setCategoryName(categoryEntity.getName());

            //设置检索属性
            skuEsModel.setAttrs(attrsList);

            return skuEsModel;
        }).collect(Collectors.toList());

        /***
         * 最终，将sku数据发送给Es进行保存（也就是商品上架）
         */
        R result = searchFeign.productStatusUp(skuEsModelList);

        if (result.getCode() == 0){
            //远程调用成功，修改当前spu的发布状态
            baseMapper.updateSpuStatus(spuId, GoodsConstant.StatusEnum.SPU_UP.getCode());
        }else {
            //远程调用失败
            //TODO 重复调用问题，接口幂等性，重试机制
        }
    }

    /***
     * 根据skuId查询Spu信息
     * @param skuId
     * @return
     */
    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);
        Long spuId = skuInfo.getSpuId();
        SpuInfoEntity spuInfo = getById(spuId);
        return spuInfo;
    }
}