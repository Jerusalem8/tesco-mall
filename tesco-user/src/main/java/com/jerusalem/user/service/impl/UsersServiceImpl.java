package com.jerusalem.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jerusalem.common.utils.HttpUtils;
import com.jerusalem.common.vo.SocialUser;
import com.jerusalem.common.vo.UserLoginVo;
import com.jerusalem.common.vo.UserRegisterVo;
import com.jerusalem.user.dao.UserLevelDao;
import com.jerusalem.user.entity.UserLevelEntity;
import com.jerusalem.user.exception.PhoneExistException;
import com.jerusalem.user.exception.UsernameExistException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.user.dao.UsersDao;
import com.jerusalem.user.entity.UsersEntity;
import com.jerusalem.user.service.UsersService;

/****
 * 服务层接口实现类
 * 用户
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {

    @Autowired
    UserLevelDao userLevelDao;

    @Autowired
    UsersDao usersDao;

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UsersEntity> page = this.page(
                new Query<UsersEntity>().getPage(params),
                new QueryWrapper<UsersEntity>()
        );
        return new PageUtils(page);
    }

    /***
     * 注册
     * @param userRegisterVo
     */
    @Override
    public void register(UserRegisterVo userRegisterVo) {
        UsersEntity usersEntity = new UsersEntity();
        //设置默认信息(默认等级)
        UserLevelEntity levelEntity = userLevelDao.getDefaultLevel();
        usersEntity.setLevelId(levelEntity.getId());
        //检查用户名和手机号是否唯一（为了让Controller感知异常，使用异常机制）
        checkEmPhoneUnique(userRegisterVo.getPhone());
        checkUsernameUnique(userRegisterVo.getUserName());
        //检查通过，保存
        usersEntity.setMobile(userRegisterVo.getPhone());
        usersEntity.setUsername(userRegisterVo.getUserName());
        //将密码进行加密存储（BCrypt盐值加密）
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(userRegisterVo.getPassword());
        usersEntity.setPassword(encode);
        //TODO 其他默认信息

        //保存
        baseMapper.insert(usersEntity);
    }

    /***
     * 手机号异常机制
     * @param phone
     */
    @Override
    public void checkEmPhoneUnique(String phone) throws PhoneExistException{
        Integer count = usersDao.selectCount(new QueryWrapper<UsersEntity>().eq("mobile", phone));
        if (count>0){
            throw new PhoneExistException();
        }
    }

    /***
     * 用户名异常机制
     * @param username
     */
    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException{
        Integer count = usersDao.selectCount(new QueryWrapper<UsersEntity>().eq("username", username));
        if (count>0){
            throw new UsernameExistException();
        }
    }

    /***
     * 登录
     * @param userLoginVo、
     */
    @Override
    public UsersEntity login(UserLoginVo userLoginVo) {
        String loginacct = userLoginVo.getLoginacct();
        String password = userLoginVo.getPassword();
        UsersEntity usersEntity = usersDao.selectOne(new QueryWrapper<UsersEntity>().eq("username", loginacct).or().eq("mobile", loginacct));
        if (usersEntity == null){
            //用户名不存在，登陆失败
            return null;
        }else {
            String passwordDb = usersEntity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(password, passwordDb);
            if (matches){
                //登陆成功
                return usersEntity;
            }else {
                //用户名或密码错误
                return null;
            }
        }
    }

    /***
     * 社交登陆
     * @param socialUser
     * @return
     */
    @Override
    public UsersEntity login(SocialUser socialUser) throws Exception {
        //登陆和注册合并
        String uid = socialUser.getUid();
        //判断当前uid是否已存在
        UsersEntity usersEntity = usersDao.selectOne(new QueryWrapper<UsersEntity>().eq("social_uid", uid));
        if (usersEntity != null){
            //该用户已注册，只需更新
            UsersEntity newUsersEntity = new UsersEntity();
            newUsersEntity.setId(usersEntity.getId());
            newUsersEntity.setAccessToken(socialUser.getAccess_token());
            newUsersEntity.setExpiresIn(socialUser.getExpires_in());
            usersDao.updateById(newUsersEntity);
            usersEntity.setAccessToken(socialUser.getAccess_token());
            usersEntity.setExpiresIn(socialUser.getExpires_in());
            return usersEntity;
        }else {
            //第一次登陆
            UsersEntity newUser = new UsersEntity();
            try {
                //获取该社交账号的用户信息
                Map<String,String> query = new HashMap<>();
                query.put("access_token",socialUser.getAccess_token());
                query.put("uid",socialUser.getUid());
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<String, String>(), query);
                if (response.getStatusLine().getStatusCode() == 200){
                    //查询成功
                    String json = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(json);
                    String name = jsonObject.getString("name");
                    String gender = jsonObject.getString("gender");
                    newUser.setNickname(name);
                    newUser.setGender("m".equals(gender)?1:0);
                    //。。。。。等等信息
                }
            }catch (Exception e){}
            //进行注册
            newUser.setSocialUid(socialUser.getUid());
            newUser.setAccessToken(socialUser.getAccess_token());
            newUser.setExpiresIn(socialUser.getExpires_in());
            usersDao.insert(newUser);
            return newUser;
        }
    }
}