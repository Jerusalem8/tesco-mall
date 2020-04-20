package com.jerusalem.user.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerusalem.user.entity.UserLevelEntity;
import com.jerusalem.user.service.UserLevelService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 用户等级
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@RestController
@RequestMapping("user/userlevel")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    /***
    * 分页查询
    * @param params
    * @return
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userLevelService.queryPage(params);

        return R.ok().put("page", page);
    }

    /***
    * 查询
    * @return
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		UserLevelEntity userLevel = userLevelService.getById(id);

        return R.ok().put("userLevel", userLevel);
    }

    /***
    * 新增
    * @return
    */
    @RequestMapping("/save")
    public R save(@RequestBody UserLevelEntity userLevel){
		userLevelService.save(userLevel);

        return R.ok();
    }

    /***
    * 修改
    * @return
    */
    @RequestMapping("/update")
    public R update(@RequestBody UserLevelEntity userLevel){
		userLevelService.updateById(userLevel);

        return R.ok();
    }

    /***
    * 删除
    * @return
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		userLevelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
