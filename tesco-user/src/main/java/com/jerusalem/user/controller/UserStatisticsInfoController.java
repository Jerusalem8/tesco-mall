package com.jerusalem.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerusalem.user.entity.UserStatisticsInfoEntity;
import com.jerusalem.user.service.UserStatisticsInfoService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 用户统计信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@RestController
@RequestMapping("user/userstatisticsinfo")
public class UserStatisticsInfoController {

    @Autowired
    private UserStatisticsInfoService userStatisticsInfoService;

    /***
    * 分页查询
    * @param params
    * @return
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userStatisticsInfoService.queryPage(params);

        return R.ok().put("page", page);
    }

    /***
    * 查询
    * @return
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		UserStatisticsInfoEntity userStatisticsInfo = userStatisticsInfoService.getById(id);

        return R.ok().put("userStatisticsInfo", userStatisticsInfo);
    }

    /***
    * 新增
    * @return
    */
    @RequestMapping("/save")
    public R save(@RequestBody UserStatisticsInfoEntity userStatisticsInfo){
		userStatisticsInfoService.save(userStatisticsInfo);

        return R.ok();
    }

    /***
    * 修改
    * @return
    */
    @RequestMapping("/update")
    public R update(@RequestBody UserStatisticsInfoEntity userStatisticsInfo){
		userStatisticsInfoService.updateById(userStatisticsInfo);

        return R.ok();
    }

    /***
    * 删除
    * @return
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		userStatisticsInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
