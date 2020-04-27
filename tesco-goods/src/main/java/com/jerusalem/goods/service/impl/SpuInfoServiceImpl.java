package com.jerusalem.goods.service.impl;

import com.jerusalem.goods.entity.*;
import com.jerusalem.goods.service.*;
import org.springframework.stereotype.Service;
import java.util.Map;
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
}