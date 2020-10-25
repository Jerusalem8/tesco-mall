package com.jerusalem.goods.service.impl;

import com.jerusalem.goods.dao.CategoryBrandRelationDao;
import com.jerusalem.goods.entity.CategoryBrandRelationEntity;
import com.jerusalem.goods.service.CategoryBrandRelationService;
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
import com.jerusalem.goods.dao.BrandDao;
import com.jerusalem.goods.entity.BrandEntity;
import com.jerusalem.goods.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * 品牌
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private CategoryBrandRelationDao relationDao;

    @Autowired
    private BrandService brandService;

    /**
    * 分页查询、关键词查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //获取key
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            queryWrapper.eq("brand_id",key).or().like("name",key).or().like("descript",key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),queryWrapper);
        return new PageUtils(page);
    }

    /**
     * 修改
     * 同步更新其他关联表中的数据，保证数据的一致性
     * @param brand
     * @return
     */
    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        //保证冗余字段的数据一致
        this.updateById(brand);
        if(!StringUtils.isEmpty(brand.getName())){
            //同步更新其他关联表中的数据
            categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());
            //TODO 更新其他关联
        }
    }

    /***
     * 根据分类ID查询品牌
     * @param categoryId
     * @return
     */
    @Override
    public List<BrandEntity> getBrandsByCategoryId(Long categoryId) {
        List<CategoryBrandRelationEntity> relationList = relationDao.selectList
                (new QueryWrapper<CategoryBrandRelationEntity>().eq("category_id", categoryId));
        List<BrandEntity> brandList = relationList.stream().map(item -> {
            Long brandId = item.getBrandId();
            BrandEntity brand = brandService.getById(brandId);
            return brand;
        }).collect(Collectors.toList());
        return brandList;
    }

    /***
     * 批量查询品牌信息（筛选栏查询）
     * @param brandIds
     * @return
     */
    @Override
    public List<BrandEntity> getBrandsByIds(List<Long> brandIds) {
        return baseMapper.selectList(new QueryWrapper<BrandEntity>().in("brand_id",brandIds));
    }
}