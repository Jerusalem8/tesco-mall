package com.jerusalem.goods.service.impl;

import com.jerusalem.common.constant.GoodsConstant;
import com.jerusalem.goods.dao.AttrGroupDao;
import com.jerusalem.goods.entity.AttrEntity;
import com.jerusalem.goods.entity.AttrGroupEntity;
import com.jerusalem.goods.service.AttrService;
import com.jerusalem.goods.vo.AttrGroupRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;
import com.jerusalem.goods.dao.AttrAttrGroupRelationDao;
import com.jerusalem.goods.entity.AttrAttrGroupRelationEntity;
import com.jerusalem.goods.service.AttrAttrGroupRelationService;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * 属性&属性分组关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("attrAttrGroupRelationService")
public class AttrAttrGroupRelationServiceImpl extends ServiceImpl<AttrAttrGroupRelationDao, AttrAttrGroupRelationEntity> implements AttrAttrGroupRelationService {

    @Autowired
    private AttrAttrGroupRelationDao relationDao;
    
    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrGroupDao attrGroupDao;

    /***
     * 根据属性分组ID查询关联属性列表
     * @param attrGroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrAttrGroupRelationEntity> relationList = relationDao.selectList
                (new QueryWrapper<AttrAttrGroupRelationEntity>().eq("attr_group_id", attrGroupId));

        List<Long> attrIds = relationList.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        if(attrIds == null || attrIds.size() == 0){
            return null;
        }
        Collection<AttrEntity> attrList = attrService.listByIds(attrIds);
        return (List<AttrEntity>) attrList;
    }

    /***
     * 删除、批量删除属性&属性分组关联关系
     * @param relationVos
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] relationVos) {
        List<AttrAttrGroupRelationEntity> relationList = Arrays.asList(relationVos).stream().map((item) -> {
            AttrAttrGroupRelationEntity relationEntity = new AttrAttrGroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(relationList);
    }

    /***
     * 查询未关联的属性
     * @param params
     * @param attrGroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long categoryId = attrGroupEntity.getCategoryId();

        //2、当前分组只能关联其他分组没有引用的属性
        //2.1、当前分类下的其他分组
        List<AttrGroupEntity> attrGroupList = attrGroupDao.selectList
                (new QueryWrapper<AttrGroupEntity>().eq("category_id", categoryId));
        List<Long> collect = attrGroupList.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //2.2、其他分组关联的属性
        List<AttrAttrGroupRelationEntity> relationList = relationDao.selectList
                (new QueryWrapper<AttrAttrGroupRelationEntity>().in("attr_group_id", collect));
        List<Long> attrIds = relationList.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2.3、从当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>();
        queryWrapper.eq("category_id", categoryId).eq("attr_type", GoodsConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if(attrIds != null && attrIds.size()>0){
            queryWrapper.notIn("attr_id", attrIds);
        }
        //2.4、获取分页参数、拼接关键词
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((w)->{
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    /***
     * 新增、批量新增关联关系
     * @param relationVos
     * @return
     */
    @Override
    public void saveBatchRelation(List<AttrGroupRelationVo> relationVos) {
        List<AttrAttrGroupRelationEntity> relationList = relationVos.stream().map(item -> {
            AttrAttrGroupRelationEntity relationEntity = new AttrAttrGroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(relationList);
    }
}