package com.jerusalem.common.vo;

import lombok.Data;

/**
 * Auto-generated: 2020-09-25 17:17:15
 * 社交用户实体类
 * @author jerusalem
 */
@Data
public class SocialUser {

    private String access_token;
    private String remind_in;
    private long expires_in;
    private String uid;
    private String isRealName;
}