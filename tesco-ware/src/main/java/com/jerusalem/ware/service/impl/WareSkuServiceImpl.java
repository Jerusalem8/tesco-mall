package com.jerusalem.ware.service.impl;

import com.jerusalem.common.utils.R;
import com.jerusalem.goods.feign.SpuInfoFeign;
import com.jerusalem.common.to.SkuStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.ware.dao.WareSkuDao;
import com.jerusalem.ware.entity.WareSkuEntity;
import com.jerusalem.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * 商品库存
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;

    @Autowired
    private SpuInfoFeign spuInfoFeign;

    /**
    * 根据仓库、SKU ID进行分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }
        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    /***
     * 商品的入库
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    @Transactional
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断,若没有这个库存记录，则新增
        List<WareSkuEntity> wareSkuEntityList = wareSkuDao.selectList(
                new QueryWrapper<WareSkuEntity>()
                        .eq("sku_id", skuId)
                        .eq("ware_id", wareId));
        if(wareSkuEntityList == null || wareSkuEntityList.size() == 0){
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            //catch异常,远程查询sku的名字，如果失败，整个事务无需回滚
            try {
                R info = spuInfoFeign.info(skuId);
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                if(info.getCode() == 0){
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){
            }
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            //新增一条库存记录
            wareSkuDao.insert(skuEntity);
        }else{
            //自定义的入库操作（添加库存）
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }
    }

    /***
     * 查询Sku是否有库存
     * @param skuIds
     * @return
     */
    @Override
    public List<SkuStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuStockVo> collect = skuIds.stream().map(skuId -> {
            SkuStockVo skuStockVo = new SkuStockVo();
            //查询当前sku的总库存量
            Long count = baseMapper.getSkuStock(skuId);
            skuStockVo.setSkuId(skuId);
            skuStockVo.setHasStock(count == null ? false : count > 0);
            return skuStockVo;
        }).collect(Collectors.toList());
        return collect;
    }
}