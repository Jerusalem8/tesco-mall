package com.jerusalem.oauth2.controller;

import com.jerusalem.common.constant.AuthConstant;
import com.jerusalem.common.exception.BizCodeEnume;
import com.jerusalem.common.utils.R;
import com.jerusalem.oauth2.vo.UserRegisterVo;
import com.jerusalem.third.feign.ThirdFeign;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    StringRedisTemplate redisTemplate;

    /***
     * 获取验证码
     * @param phone
     * @return
     */
    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone")String phone){
        //TODO 接口防刷
        /**
         * 验证码的校验
         * 加上系统时间，防止统一手机号在60s内再次发送验证码
         */
        String redisCode = redisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000){
                //验证码已存在，60秒内不可再发
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        //验证码不存在，生成随机验证码
        String newRedisCode = UUID.randomUUID().toString().substring(0, 5)+"_"+System.currentTimeMillis();
        String code = StringUtils.substring(newRedisCode,0,5);
        /**
         *
         * redis暂存验证（key，value，过期时间，时间单位）
         */
        redisTemplate.opsForValue().set(AuthConstant.SMS_CODE_CACHE_PREFIX+phone,newRedisCode,10, TimeUnit.MINUTES);
        thirdFeign.sendCode(phone,code);
        return R.ok();
    }


    /***
     * 注册（坑巨多）
     * RedirectAttributes：模拟重定向携带数据
     * //TODO 重定向携带数据，利用session原理，将数据放在session中
     * 跳转到下一个页面，取出数据以后，session就会被删除
     * //TODO 分布式下的Session问题
     *
     * @param userRegisterVo
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo userRegisterVo, BindingResult result, RedirectAttributes attributes){
        //先校验格式
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,FieldError::getDefaultMessage));
//            model.addAttribute("errors",errors);
            attributes.addFlashAttribute("errors",errors);
            //校验出错，转发到注册页
//            return "forward:http://auth.tesco.com/register.html";(转发：路径映射默认请求方式为get，所以会报错)
            return "redirect:http://auth.tesco.com/register.html";
        }
        //校验验证码
        String code = userRegisterVo.getCode();
        String s = redisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
        if (!StringUtils.isEmpty(s)){
            if(code.equals(s.split("_")[0])){
                //删除验证码（令牌机制）
                redisTemplate.delete(AuthConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
                //验证码通过，真正开始调用远程服务进行注册

            }else {
                Map<String,String> errors = new HashMap<>();
                errors.put("code","验证码错误");
                attributes.addFlashAttribute("errors",errors);
                //校验出错，转发到注册页
                return "redirect:http://auth.tesco.com/register.html";
            }
        }else {
            Map<String,String> errors = new HashMap<>();
            errors.put("code","验证码错误");
            attributes.addFlashAttribute("errors",errors);
            //校验出错，转发到注册页
            return "redirect:http://auth.tesco.com/register.html";
        }
        //注册成功，重定向到登录页
        return "redirect:http://auth.tesco.com/login.html";
    }
}
