package com.jerusalem.goods.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.jerusalem.common.utils.R;
import com.jerusalem.goods.entity.SkuImagesEntity;
import com.jerusalem.goods.entity.SpuInfoDescEntity;
import com.jerusalem.goods.service.*;
import com.jerusalem.goods.vo.SeckillSkuInfoVo;
import com.jerusalem.goods.vo.SkuItemVo;
import com.jerusalem.goods.vo.SkuSaleAttrVo;
import com.jerusalem.goods.vo.SpuBaseAttrGroupVo;
import com.jerusalem.seckill.feign.SeckillFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;
import com.jerusalem.goods.dao.SkuInfoDao;
import com.jerusalem.goods.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * sku信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    SeckillFeign seckillFeign;

    /**
    * 根据分类、品牌、价格、关键词进行分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        //关键词
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        //分类
        String categoryId = (String) params.get("categoryId");
        if(!StringUtils.isEmpty(categoryId) && !"0".equalsIgnoreCase(categoryId)){
            queryWrapper.eq("category_id",categoryId);
        }
        //品牌
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(categoryId)){
            queryWrapper.eq("brand_id",brandId);
        }
        //价格区间
        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
            //ge ---> 大于等于
            queryWrapper.ge("price",min);
        }
        String max = (String) params.get("max");
        if(!StringUtils.isEmpty(max)){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);
                //max默认为0，所以要与0作比较，只有当max不为0（compareTo返回值为1）时，才进行后续的价格筛选操作
                if(bigDecimal.compareTo(new BigDecimal("0")) == 1){
                    //ge ---> 小于等于
                    queryWrapper.le("price",max);
                }
            }catch (Exception e){
            }
        }

        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    /***
     * 保存SKU的基本信息
     * @param skuInfo
     */
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfo) {
        this.baseMapper.insert(skuInfo);
    }

    /***
     * 根据spuId查询sku集合
     * @param spuId
     * @return
     */
    @Override
    public List<SkuInfoEntity> getSkuListBySpuId(Long spuId) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id",spuId);
        List<SkuInfoEntity> skuList = this.list(queryWrapper);
        return skuList;
    }

    /***
     * 商品详情页信息封装
     * 异步编排
     * @param skuId
     * @return
     */
    @Override
    public SkuItemVo skuItem(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();
        /***
         * 异步任务编排
         * 任务1.1 1.2 1.3 并列，等待任务1执行完毕
         */
        //任务1（有返回结果）
        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            //1.sku的基本信息（标题，副标题，价格)
            SkuInfoEntity skuInfo = getById(skuId);
            skuItemVo.setSkuInfo(skuInfo);
            return skuInfo;
        }, threadPoolExecutor);
        //任务1.1
        CompletableFuture<Void> skuSaleAttrFuture = skuInfoFuture.thenAcceptAsync((result)->{
            //3.spu的销售属性
            List<SkuSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrVo(result.getSpuId());
            skuItemVo.setSkuSaleAttrVos(saleAttrVos);
        },threadPoolExecutor);
        //任务1.2
        CompletableFuture<Void> spuInfoDescFuture = skuInfoFuture.thenAcceptAsync((result) -> {
            //4.spu的介绍
            SpuInfoDescEntity spuInfoDesc = spuInfoDescService.getById(result.getSpuId());
            skuItemVo.setSpuInfoDesc(spuInfoDesc);
        }, threadPoolExecutor);
        //任务1.3
        CompletableFuture<Void> spuBaseAttrGroupFuture = skuInfoFuture.thenAcceptAsync((reslut) -> {
            //5.spu的规格参数
            List<SpuBaseAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupVos(reslut.getSpuId(), reslut.getCategoryId());
            skuItemVo.setSpuBaseAttrGroupVos(attrGroupVos);
        }, threadPoolExecutor);

        //任务2(无返回结果)
        CompletableFuture<Void> skuImagesFuture = CompletableFuture.runAsync(() -> {
            //2.sku的图片信息
            List<SkuImagesEntity> skuImages = skuImagesService.getBySkuId(skuId);
            skuItemVo.setSkuImages(skuImages);
        }, threadPoolExecutor);

        //任务3 查询sku是否参与秒杀系统
        CompletableFuture<Void> seckillSkuInfoFuture = CompletableFuture.runAsync(() -> {
            R r = seckillFeign.getSkuSeckillInfo(skuId);
            if (r.getCode() == 0){
                SeckillSkuInfoVo seckillSkuInfoVo = r.getData(new TypeReference<SeckillSkuInfoVo>() {
                });
                skuItemVo.setSeckillSkuInfo(seckillSkuInfoVo);
            }
        }, threadPoolExecutor);

        //等待所有任务执行完成
        CompletableFuture.allOf(skuInfoFuture,skuSaleAttrFuture,spuInfoDescFuture,spuBaseAttrGroupFuture,skuImagesFuture,seckillSkuInfoFuture).get();

        return skuItemVo;
    }
}