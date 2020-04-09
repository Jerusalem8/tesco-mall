package com.jerusalem.goods.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("goods/attr/group/relation")
public class AttrAttrGroupRelationController {

    @Autowired
    private AttrAttrGroupRelationService attrAttrGroupRelationService;

    /***
    * 分页查询
    * @param params
    * @return
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrAttrGroupRelationService.queryPage(params);

        return R.ok().put("page", page);
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
