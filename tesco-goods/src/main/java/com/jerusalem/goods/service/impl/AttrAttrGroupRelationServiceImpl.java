package com.jerusalem.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.goods.dao.AttrAttrGroupRelationDao;
import com.jerusalem.goods.entity.AttrAttrGroupRelationEntity;
import com.jerusalem.goods.service.AttrAttrGroupRelationService;

/****
 * 服务层接口实现类
 * 属性&属性分组关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("attrAttrGroupRelationService")
public class AttrAttrGroupRelationServiceImpl extends ServiceImpl<AttrAttrGroupRelationDao, AttrAttrGroupRelationEntity> implements AttrAttrGroupRelationService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrGroupRelationEntity> page = this.page(
                new Query<AttrAttrGroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrGroupRelationEntity>()
        );
        return new PageUtils(page);
    }

}