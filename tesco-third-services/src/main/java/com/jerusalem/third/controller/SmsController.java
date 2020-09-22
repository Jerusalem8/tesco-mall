package com.jerusalem.third.controller;

import com.jerusalem.common.utils.R;
import com.jerusalem.third.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/****
 * @Author: jerusalem
 * @Description: SmsController
 * 获取验证码接口
 * @Date 2020/9/22 16:34
 *****/
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    SmsComponent smsComponent;

    /***
     * 为其他服务提供获取验证码的接口
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sendcode")
    public R sendCode(@RequestParam("phone")String phone, @RequestParam("code")String code){
        smsComponent.sendSmsCode(phone, code);
        return R.ok();
    }
}
