package com.jerusalem.oauth2.vo;

import lombok.Data;

/****
 * @Author: jerusalem
 * @Description: UserLoginVo
 * 用户登录信息封装
 * @Date 2020/9/23 17:20
 *****/
@Data
public class UserLoginVo {

    /**
     * 登录账号
     * 密码
     */
    private String loginacct;
    private String password;

}
