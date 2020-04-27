package com.jerusalem.goods.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.goods.dao.SkuSaleAttrValueDao;
import com.jerusalem.goods.entity.SkuSaleAttrValueEntity;
import com.jerusalem.goods.service.SkuSaleAttrValueService;

/****
 * 服务层接口实现类
 * sku销售属性&值
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

}