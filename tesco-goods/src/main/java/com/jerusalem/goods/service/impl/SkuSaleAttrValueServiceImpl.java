package com.jerusalem.goods.service.impl;

import com.jerusalem.goods.vo.SkuSaleAttrVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.goods.dao.SkuSaleAttrValueDao;
import com.jerusalem.goods.entity.SkuSaleAttrValueEntity;
import com.jerusalem.goods.service.SkuSaleAttrValueService;

import java.util.List;

/****
 * 服务层接口实现类
 * sku销售属性&值
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    /***
     * 获取销售属性集合
     * @param spuId
     * @return
     */
    @Override
    public List<SkuSaleAttrVo> getSaleAttrVo(Long spuId) {
        List<SkuSaleAttrVo> skuSaleAttrVos = baseMapper.getSaleAttrVoBySpuId(spuId);
        return skuSaleAttrVos;
    }

    /***
     * 获取sku销售属性集合
     * @param skuId
     * @return
     */
    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {
        SkuSaleAttrValueDao dao = this.baseMapper;
        return dao.getSkuSaleAttrValuesAsStringList(skuId);
    }
}