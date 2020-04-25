package com.jerusalem.coupon.controller;

import com.jerusalem.coupon.entity.HomeSubjectEntity;
import com.jerusalem.coupon.service.HomeSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;
import java.util.Arrays;
import java.util.Map;


/****
 * 控制层
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@RestController
@RequestMapping("sale/homesubject")
public class HomeSubjectController {

    @Autowired
    private HomeSubjectService homeSubjectService;

    /***
    * 分页查询
    * @param params
    * @return
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = homeSubjectService.queryPage(params);

        return R.ok().put("page", page);
    }

    /***
    * 查询
    * @return
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		HomeSubjectEntity homeSubject = homeSubjectService.getById(id);

        return R.ok().put("homeSubject", homeSubject);
    }

    /***
    * 新增
    * @return
    */
    @RequestMapping("/save")
    public R save(@RequestBody HomeSubjectEntity homeSubject){
		homeSubjectService.save(homeSubject);

        return R.ok();
    }

    /***
    * 修改
    * @return
    */
    @RequestMapping("/update")
    public R update(@RequestBody HomeSubjectEntity homeSubject){
		homeSubjectService.updateById(homeSubject);

        return R.ok();
    }

    /***
    * 删除
    * @return
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		homeSubjectService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
