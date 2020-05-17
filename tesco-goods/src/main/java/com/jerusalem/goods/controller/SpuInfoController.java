package com.jerusalem.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jerusalem.goods.entity.ProductAttrValueEntity;
import com.jerusalem.goods.service.ProductAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jerusalem.goods.entity.SpuInfoEntity;
import com.jerusalem.goods.service.SpuInfoService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;

/****
 * 控制层
 * spu信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@RestController
@RequestMapping("goods/spu/info")
public class SpuInfoController {

    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    /***
    * 根据分类、品牌、状态、关键词进行分页查询
    * @param params
    * @return
    */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }

    /***
     * 查询spu的信息(用于仓储微服务远程查询spuName)
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        SpuInfoEntity spuInfo = spuInfoService.getById(id);
        return R.ok().put("spuInfo", spuInfo);
    }

    /***
     * 查询spu的规格信息
     * @param spuId
     * @return
     */
    @GetMapping("/base/attr/list/{spuId}")
    public R baseAttrList(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> baseAttrList = productAttrValueService.baseAttrList(spuId);
        return R.ok().put("data",baseAttrList);
    }

    /***
     * 修改spu的规格信息
     * @param spuId
     * @param spuAttrList
     * @return
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> spuAttrList){
        productAttrValueService.updateSpuAttr(spuId,spuAttrList);
        return R.ok();
    }

    /***
     * 商品上架（发送sku数据到Es）
     * @param spuId
     * @return
     */
    @PostMapping("/up/{spuId}")
    public R upSpu(@PathVariable("spuId") Long spuId){
        spuInfoService.upSpu(spuId);
        return R.ok();
    }

    /***
    * 修改
    * @return
    */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /***
    * 删除
    * @return
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
