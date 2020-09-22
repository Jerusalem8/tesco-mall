package com.jerusalem.user.controller;

import java.util.Arrays;
import java.util.Map;

import com.jerusalem.user.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jerusalem.user.entity.UsersEntity;
import com.jerusalem.user.service.UsersService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 用户
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@RestController
@RequestMapping("user/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public R register(@RequestBody UserRegisterVo userRegisterVo){
        usersService.register(userRegisterVo);
        return R.ok();
    }




    /***
    * 分页查询
    * @param params
    * @return
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = usersService.queryPage(params);

        return R.ok().put("page", page);
    }

    /***
    * 查询
    * @return
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		UsersEntity users = usersService.getById(id);

        return R.ok().put("users", users);
    }

    /***
    * 新增
    * @return
    */
    @RequestMapping("/save")
    public R save(@RequestBody UsersEntity users){
		usersService.save(users);

        return R.ok();
    }

    /***
    * 修改
    * @return
    */
    @RequestMapping("/update")
    public R update(@RequestBody UsersEntity users){
		usersService.updateById(users);

        return R.ok();
    }

    /***
    * 删除
    * @return
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		usersService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
