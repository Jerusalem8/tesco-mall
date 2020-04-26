package com.jerusalem.goods.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.goods.dao.SkuInfoDao;
import com.jerusalem.goods.entity.SkuInfoEntity;
import com.jerusalem.goods.service.SkuInfoService;
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
            queryWrapper.eq("catalog_id",categoryId);
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
}