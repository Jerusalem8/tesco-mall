package com.jerusalem.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.vo.SocialUser;
import com.jerusalem.common.vo.UserLoginVo;
import com.jerusalem.common.vo.UserRegisterVo;
import com.jerusalem.user.entity.UsersEntity;
import com.jerusalem.user.exception.PhoneExistException;
import com.jerusalem.user.exception.UsernameExistException;

import java.util.Map;

/****
 * 服务层接口
 * 用户
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
public interface UsersService extends IService<UsersEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);

    /***
     * 注册
     * @param userRegisterVo
     */
    void register(UserRegisterVo userRegisterVo);

    /***
     * 检测唯一性
     * 邮箱，用户名
     */
    void checkEmPhoneUnique(String phone) throws PhoneExistException;
    void checkUsernameUnique(String username) throws UsernameExistException;

    /***
     * 账号密码登录
     * @param userLoginVo
     */
    UsersEntity login(UserLoginVo userLoginVo);

    /***
     * 社交登陆
     * @param socialUser
     * @return
     */
    UsersEntity login(SocialUser socialUser) throws Exception;
}

