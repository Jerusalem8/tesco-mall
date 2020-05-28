package com.jerusalem.goods.controller.server;

import java.util.List;
import java.util.Map;
import com.jerusalem.goods.entity.AttrEntity;
import com.jerusalem.goods.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    /***
    * 根据属性分组ID查询关联属性列表
    * @param attrGroupId
    * @return
    */
    @GetMapping("/list/{attrGroupId}")
    public R attrList(@PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> attrList =  attrAttrGroupRelationService.getRelationAttr(attrGroupId);
        return R.ok().put("data",attrList);
    }

    /***
     * 查询未关联的属性
     * @param attrGroupId
     * @param params
     * @return
     */
    @GetMapping("/no/list/{attrGroupId}")
    public R noRelationList(@PathVariable("attrGroupId") Long attrGroupId,
                  @RequestParam Map<String, Object> params){
        PageUtils page = attrAttrGroupRelationService.getNoRelationAttr(params,attrGroupId);
        return R.ok().put("page",page);
    }

    /***
     * 新增、批量新增关联关系
     * @param relationVos
     * @return
     */
    @PostMapping("/save")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> relationVos){
        attrAttrGroupRelationService.saveBatchRelation(relationVos);
        return R.ok();
    }

    /***
     * 删除属性&属性分组关联关系
     * @param relationVos
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody AttrGroupRelationVo[] relationVos){
		attrAttrGroupRelationService.deleteRelation(relationVos);
        return R.ok();
    }
}
