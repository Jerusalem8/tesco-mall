package com.jerusalem.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.goods.dao.ProductAttrValueDao;
import com.jerusalem.goods.entity.ProductAttrValueEntity;
import com.jerusalem.goods.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;

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

    /***
     * 查询spu的规格信息
     * @param spuId
     * @return
     */
    @Override
    public List<ProductAttrValueEntity> baseAttrList(Long spuId) {
        List<ProductAttrValueEntity> productAttrValueList = this.baseMapper.selectList(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return productAttrValueList;
    }

    /***
     * 修改spu的规格信息
     * @param spuId
     * @param spuAttrList
     * @return
     */
    @Transactional
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> spuAttrList) {
        //1、先删除这个spuId之前对应的所有属性
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));
        //2、写入新的属性并批量保存
        List<ProductAttrValueEntity> collect = spuAttrList.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }
}