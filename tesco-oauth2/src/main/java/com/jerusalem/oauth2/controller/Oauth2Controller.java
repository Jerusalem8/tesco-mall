package com.jerusalem.oauth2.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jerusalem.common.constant.AuthConstant;
import com.jerusalem.common.exception.BizCodeEnume;
import com.jerusalem.common.utils.HttpUtils;
import com.jerusalem.common.utils.R;
import com.jerusalem.common.vo.SocialUser;
import com.jerusalem.common.vo.UserLoginVo;
import com.jerusalem.common.vo.UserRegisterVo;
import com.jerusalem.common.vo.UserResponseVo;
import com.jerusalem.third.feign.ThirdFeign;
import com.jerusalem.user.feign.UsersFeign;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
public class Oauth2Controller {

    @Autowired
    ThirdFeign thirdFeign;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    UsersFeign usersFeign;

    /***
     * 获取注册验证码
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
                R r = usersFeign.register(userRegisterVo);
                if (r.getCode() == 0){
                    //成功
                    return "redirect:http://auth.tesco.com/login.html";
                }else {
                    Map<String,String> errors = new HashMap<>();
                    errors.put("msg",r.getData("msg",new TypeReference<String>(){}));

                    attributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.tesco.com/register.html";
                }
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
    }

    /***
     * 账号密码登录
     * @param userLoginVo
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/login")
    public String login(UserLoginVo userLoginVo,RedirectAttributes redirectAttributes,HttpSession httpSession){
        //远程登录
        R login = usersFeign.login(userLoginVo);
        if (login.getCode() == 0){
            //成功
            UserResponseVo data = login.getData("data", new TypeReference<UserResponseVo>(){});
            httpSession.setAttribute(AuthConstant.LOGIN_USER,data);
            return "redirect:http://tesco.com";
        }else {
            Map<String,String> errors = new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.tesco.com/login.html";
        }
    }

    /***
     * 社交登陆-微博
     * @param code
     * @return
     */
    @GetMapping("/oauth/weibo/success")
    public String loginByWeibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> header = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        //1.根据授权码获取访问令牌
        Map<String, String> map = new HashMap<>();
        map.put("client_id","3650192197");
        map.put("client_secret","85b88f83cc4f2f02c83dd57af9a0735e");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://auth.tesco.com/oauth/weibo/success");
        map.put("code",code);
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", header, query, map);
        //2.处理请求结果
        if (response.getStatusLine().getStatusCode() == 200){
            //获取成功,处理访问令牌
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            //如果当前社交用户是第一次登入本网站，将进行自动注册（为当前社交用户生成一个用户信息账号，并绑定）
            R r = usersFeign.oauthLogin(socialUser);
            if (r.getCode() == 0){
                //登陆成功，跳转回首页
                UserResponseVo data = r.getData("data", new TypeReference<UserResponseVo>() {});
                //TODO 解决子域 Session共享问题
                //TODO 使用json的序列化方式来序列化对象到redis中
                session.setAttribute(AuthConstant.LOGIN_USER,data);
                return "redirect:http://tesco.com";
            }else {
                //登陆失败
                return "redirect:http://auth.tesco.com/login.html";
            }
        }else {
            //获取失败
            return "redirect:http://auth.tesco.com/login.html";
        }
    }

    /***
     * 登录与未登录状态下登录页跳转处理
     * @param httpSession
     * @return
     */
    @GetMapping("/login.html")
    public String loginPage(HttpSession httpSession){
        Object attribute = httpSession.getAttribute(AuthConstant.LOGIN_USER);
        if (attribute ==null){
            //未登录
            return "login";
        }else {
            //已登录
            return "redirect:http://tesco.com";
        }
    }

}
