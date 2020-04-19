package com.jerusalem.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jerusalem.goods.entity.AttrEntity;
import com.jerusalem.goods.entity.CategoryBrandRelationEntity;
import com.jerusalem.goods.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jerusalem.goods.entity.AttrAttrGroupRelationEntity;
import com.jerusalem.goods.service.AttrAttrGroupRelationService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 属性&属性分组关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@RestController
@RequestMapping("goods/attr/attr/group/relation")
public class AttrAttrGroupRelationController {

    @Autowired
    private AttrAttrGroupRelationService attrAttrGroupRelationService;

    @Autowired
    private AttrService attrService;

    /***
    * 获取属性分组的关联属性列表
    * @param attrGroupId
    * @return
    */
    @GetMapping("/{attrgroupId}")
    public R attrList(@RequestParam("attrGroupId") Long attrGroupId){
//        List<AttrEntity> entities =  attrService.getRelationAttr(attrgroupId);
//        return R.ok().put("data",entities);
        return null;
    }

    /***
    * 查询
    * @return
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		AttrAttrGroupRelationEntity attrAttrGroupRelation = attrAttrGroupRelationService.getById(id);

        return R.ok().put("attrAttrGroupRelation", attrAttrGroupRelation);
    }

    /***
    * 新增
    * @return
    */
    @RequestMapping("/save")
    public R save(@RequestBody AttrAttrGroupRelationEntity attrAttrGroupRelation){
		attrAttrGroupRelationService.save(attrAttrGroupRelation);

        return R.ok();
    }

    /***
    * 修改
    * @return
    */
    @RequestMapping("/update")
    public R update(@RequestBody AttrAttrGroupRelationEntity attrAttrGroupRelation){
		attrAttrGroupRelationService.updateById(attrAttrGroupRelation);

        return R.ok();
    }

    /***
    * 删除
    * @return
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		attrAttrGroupRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
