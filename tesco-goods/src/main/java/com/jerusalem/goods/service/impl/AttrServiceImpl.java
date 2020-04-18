package com.jerusalem.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jerusalem.common.constant.GoodsConstant;
import com.jerusalem.goods.dao.AttrAttrGroupRelationDao;
import com.jerusalem.goods.dao.AttrGroupDao;
import com.jerusalem.goods.dao.CategoryDao;
import com.jerusalem.goods.entity.AttrAttrGroupRelationEntity;
import com.jerusalem.goods.entity.AttrGroupEntity;
import com.jerusalem.goods.entity.CategoryEntity;
import com.jerusalem.goods.service.CategoryService;
import com.jerusalem.goods.vo.AttrResponseVo;
import com.jerusalem.goods.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;
import com.jerusalem.goods.dao.AttrDao;
import com.jerusalem.goods.entity.AttrEntity;
import com.jerusalem.goods.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/****
 * 服务层接口实现类
 * 商品属性
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrGroupRelationDao attrAttrGroupRelationDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryService categoryService;

    /***
     * 根据分类ID、关键词分页查询基本属性和销售属性
     * @param params
     * @param categoryId
     * @param attrType
     * @return
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId, String attrType) {
        QueryWrapper<AttrEntity> queryWrapper =
                new QueryWrapper<AttrEntity>()
                        .eq("attr_type", "base".equalsIgnoreCase(attrType)?
                                        GoodsConstant.AttrEnum.ATTR_TYPE_BASE.getCode():
                                        GoodsConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if(categoryId != 0){
            queryWrapper.eq("category_id",categoryId);
        }
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVo> respVos = records.stream().map((attrEntity) -> {
            AttrResponseVo attrRespVo = new AttrResponseVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //设置分类和分组的名字
            if("base".equalsIgnoreCase(attrType)){
                AttrAttrGroupRelationEntity attrId = attrAttrGroupRelationDao.selectOne(new QueryWrapper<AttrAttrGroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId()!=null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }

            }


            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCategoryId());
            if (categoryEntity != null) {
                attrRespVo.setCategoryName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    /***
     * 查询属性的信息（实现修改时数据的回显）
     * @param attrId
     * @return
     */
    @Override
    public AttrResponseVo getAttrInfo(Long attrId) {
        AttrResponseVo respVo = new AttrResponseVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,respVo);


        if(attrEntity.getAttrType() == GoodsConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //1、设置分组信息
            AttrAttrGroupRelationEntity attrGroupRelation = attrAttrGroupRelationDao.selectOne(new QueryWrapper<AttrAttrGroupRelationEntity>().eq("attr_id", attrId));
            if(attrGroupRelation!=null){
                respVo.setAttrGroupId(attrGroupRelation.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupRelation.getAttrGroupId());
                if(attrGroupEntity!=null){
                    respVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        //2、设置分类信息
        Long categoryId = attrEntity.getCategoryId();
        Long[] categoryPath = categoryService.findCategoryPath(categoryId);
        respVo.setCategoryPath(categoryPath);

        CategoryEntity categoryEntity = categoryDao.selectById(categoryId);
        if(categoryEntity!=null){
            respVo.setCategoryName(categoryEntity.getName());
        }
        return respVo;
    }


    /***
     * 新增属性（属性表、关联表）
     * @param attr
     */
    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        //复制来源于前端网页的数据
        BeanUtils.copyProperties(attr,attrEntity);
        //1、保存基本数据
        this.save(attrEntity);
        //2、保存关联关系
        if(attr.getAttrType() == GoodsConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId()!=null){
            AttrAttrGroupRelationEntity relationEntity = new AttrAttrGroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrGroupRelationDao.insert(relationEntity);
        }
    }

    /***
     * 修改属性的信息
     * @param attr
     */
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        if(attrEntity.getAttrType() == GoodsConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //1、修改分组关联
            AttrAttrGroupRelationEntity relationEntity = new AttrAttrGroupRelationEntity();

            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());

            Integer count = attrAttrGroupRelationDao.selectCount(new QueryWrapper<AttrAttrGroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if(count>0){

                attrAttrGroupRelationDao.update(relationEntity,new UpdateWrapper<AttrAttrGroupRelationEntity>().eq("attr_id",attr.getAttrId()));

            }else{
                attrAttrGroupRelationDao.insert(relationEntity);
            }
        }
    }
}