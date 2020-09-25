package com.jerusalem.common.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/****
 * @Author: jerusalem
 * @Description: UserRegister
 * 接收用户注册时前端传回的用户信息
 * @Date 2020/9/22 19:06
 *****/
@Data
public class UserRegisterVo {

    @NotEmpty(message = "用户名必须提交")
    @Length(min = 6,max = 16,message = "用户名必须是6-18位字符")
    private String userName;

    @NotEmpty(message = "密码必须填写")
    @Length(min = 6,max = 16,message = "密码必须是6-16位字符")
    private String password;

    @NotEmpty(message = "手机号必须填写")
    @Pattern(regexp = "^[1]([3-9])|[0-9]{9}$",message = "手机号格式不正确")
    private String phone;

    @NotEmpty(message = "验证码不能为空")
    private String code;
}
