package com.jerusalem.user.feign;

import com.jerusalem.common.utils.R;
import com.jerusalem.common.vo.SocialUser;
import com.jerusalem.common.vo.UserLoginVo;
import com.jerusalem.common.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author: jerusalem
 * @Description: UsersFeign业务层接口
 * 用户接口，用作远程微服务调用
 * @Date 2020/9/23 15:56
 *****/
@FeignClient(name = "user-service")
@RequestMapping("user/users")
public interface UsersFeign {

    /**
     * 注册
     * @param userRegisterVo
     * @return
     */
    @PostMapping("/register")
    R register(@RequestBody UserRegisterVo userRegisterVo);

    /***
     * 账号密码登录
     * @param userLoginVo
     * @return
     */
    @PostMapping("/login")
    R login(@RequestBody UserLoginVo userLoginVo);

    /***
     * 社交登录
     * @param socialUser
     * @return
     */
    @PostMapping("/oauth/login")
    R oauthLogin(@RequestBody SocialUser socialUser) throws Exception;
}
