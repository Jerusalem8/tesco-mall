package com.jerusalem.oauth2.controller;

import com.jerusalem.common.utils.R;
import com.jerusalem.third.feign.ThirdFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/****
 * @Author: jerusalem
 * @Description: LoginController
 * 登录控制器
 * @Date 2020/9/22 9:57
 *****/
@Controller
public class LoginController {

    @Autowired
    ThirdFeign thirdFeign;

    /***
     * 发送验证码
     * @param phone
     * @return
     */
    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone")String phone){
        //生成随机验证码
        String code = UUID.randomUUID().toString().substring(0, 5);
        thirdFeign.sendCode(phone,code);
        return R.ok();
    }
}
