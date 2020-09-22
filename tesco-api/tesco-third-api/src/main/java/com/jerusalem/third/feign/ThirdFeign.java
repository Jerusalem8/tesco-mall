package com.jerusalem.third.feign;

import com.jerusalem.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/****
 * @Author: jerusalem
 * @Description: ThirdFeign业务层接口
 * @Date 2020/9/22 16:45
 *****/
@FeignClient(name="third-service")
public interface ThirdFeign {

    /***
     * 为其他服务提供获取验证码的接口
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sms/sendcode")
    R sendCode(@RequestParam("phone")String phone, @RequestParam("code")String code);
}
