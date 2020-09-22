package com.jerusalem.user.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/****
 * @Author: jerusalem
 * @Description: UserRegisterVo
 * @Date 2020/9/22 20:39
 *****/
@Data
public class UserRegisterVo {

    private String userName;
    private String password;
    private String phone;
}
