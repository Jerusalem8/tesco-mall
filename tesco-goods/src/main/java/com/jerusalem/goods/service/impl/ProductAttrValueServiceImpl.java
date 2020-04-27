package com.jerusalem.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.goods.dao.ProductAttrValueDao;
import com.jerusalem.goods.entity.ProductAttrValueEntity;
import com.jerusalem.goods.service.ProductAttrValueService;

/****
 * 服务层接口实现类
 * spu属性值
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    /***
     * 保存商品的属性及其可选值
     * @param attrValueList
     */
    @Override
    public void saveAttrValue(List<ProductAttrValueEntity> attrValueList) {
        this.saveBatch(attrValueList);
    }
}