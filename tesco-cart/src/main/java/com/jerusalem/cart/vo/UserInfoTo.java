package com.jerusalem.cart.vo;

import lombok.Data;

/****
 * @Author: jerusalem
 * @Description: UserInfoVo
 * 用户不同登录状态的信息封装(传输对象)
 * @Date 2020/9/30 19:26
 *****/
@Data
public class UserInfoTo {

    /**
     * 已登录，用户ID
     */
    private Long userId;

    /**
     * 未登录，用户标识
     */
    private String userKey;

    /**
     * 是否有临时用户
     */
    private boolean tempUser = false;
}
