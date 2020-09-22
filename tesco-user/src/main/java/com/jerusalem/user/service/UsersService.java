package com.jerusalem.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.user.entity.UsersEntity;
import com.jerusalem.user.vo.UserRegisterVo;

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
    void checkEmPhoneUnique(String phone);
    void checkUsernameUnique(String username);
}

