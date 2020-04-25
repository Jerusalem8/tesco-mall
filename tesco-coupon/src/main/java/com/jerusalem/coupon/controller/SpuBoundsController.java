package com.jerusalem.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jerusalem.coupon.entity.SpuBoundsEntity;
import com.jerusalem.coupon.service.SpuBoundsService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 商品spu积分设置
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:47:38
 */
@RestController
@RequestMapping("coupon/spu/bounds")
public class SpuBoundsController {

    @Autowired
    private SpuBoundsService spuBoundsService;

    /***
     * 保存积分信息
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuBoundsEntity spuBoundsTo){
        spuBoundsService.save(spuBoundsTo);
        return R.ok();
    }




    /***
     * 分页查询
     * @param params
     * @return
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }

    /***
     * 查询
     * @return
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        SpuBoundsEntity spuBounds = spuBoundsService.getById(id);

        return R.ok().put("spuBounds", spuBounds);
    }

    /***
     * 修改
     * @return
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuBoundsEntity spuBounds){
        spuBoundsService.updateById(spuBounds);

        return R.ok();
    }

    /***
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        spuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
