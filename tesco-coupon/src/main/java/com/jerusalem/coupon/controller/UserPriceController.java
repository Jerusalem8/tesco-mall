package com.jerusalem.coupon.controller;

import com.jerusalem.coupon.entity.UserPriceEntity;
import com.jerusalem.coupon.service.UserPriceService;
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
 * 商品用户价格
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@RestController
@RequestMapping("sale/userprice")
public class UserPriceController {

    @Autowired
    private UserPriceService userPriceService;

    /***
    * 分页查询
    * @param params
    * @return
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userPriceService.queryPage(params);

        return R.ok().put("page", page);
    }

    /***
    * 查询
    * @return
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		UserPriceEntity userPrice = userPriceService.getById(id);

        return R.ok().put("userPrice", userPrice);
    }

    /***
    * 新增
    * @return
    */
    @RequestMapping("/save")
    public R save(@RequestBody UserPriceEntity userPrice){
		userPriceService.save(userPrice);

        return R.ok();
    }

    /***
    * 修改
    * @return
    */
    @RequestMapping("/update")
    public R update(@RequestBody UserPriceEntity userPrice){
		userPriceService.updateById(userPrice);

        return R.ok();
    }

    /***
    * 删除
    * @return
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		userPriceService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
