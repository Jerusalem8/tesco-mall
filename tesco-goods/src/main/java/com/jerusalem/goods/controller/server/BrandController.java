package com.jerusalem.goods.controller.server;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.jerusalem.common.valid.AddGroup;
import com.jerusalem.common.valid.UpdateGroup;
import com.jerusalem.common.valid.UpdateStatusGroup;
import com.jerusalem.goods.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.jerusalem.goods.entity.BrandEntity;
import com.jerusalem.goods.service.BrandService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;

/****
 * 控制层
 * 品牌
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@RestController
@RequestMapping("goods/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /***
    * 根据关键词分页查询品牌列表
    * @param params
    * @return
    */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }

    /***
     * 根据分类查询出相应的品牌
     * @param categoryId
     * @return
     */
    @GetMapping("/relation/list")
    public R brandList(@RequestParam (value = "categoryId", required = true) Long categoryId){
        List<BrandEntity> brandList = brandService.getBrandsByCategoryId(categoryId);

        List<BrandVo> brandVos = brandList.stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data",brandVos);
    }

    /***
     * 查询品牌信息（实现品牌修改时数据的回显）
     * @param brandId
     * @return
     */
    @GetMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
        BrandEntity brand = brandService.getById(brandId);
        return R.ok().put("brand", brand);
    }

    /***
     * 批量查询品牌信息（筛选栏查询）
     * @param brandIds
     * @return
     */
    @GetMapping("/infos")
    public R infos(@RequestParam("brandIds") List<Long> brandIds){
        List<BrandEntity> brandList = brandService.getBrandsByIds(brandIds);
        return R.ok().put("brand", brandList);
    }

    /***
    * 品牌新增
    * @return
    */
    @PostMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 品牌修改
     * 同步更新其他关联表中的数据，保证数据的一致性
     * @param brand
     * @return
     */
    @PostMapping("/update")
    public R update(@Validated({UpdateGroup.class})@RequestBody BrandEntity brand){
		brandService.updateDetail(brand);
        return R.ok();
    }

    /**
     * 修改品牌显示状态
     * 与品牌修改区分开，该方法提交的参数，除显示状态，其他字段可为空
     * @param brand
     * @return
     */
    @PostMapping("/update/status")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }

    /***
    * 删除、批量删除品牌
    * @return
    */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));
        return R.ok();
    }
}
