package com.jerusalem.cart.interceptor;

import com.jerusalem.cart.vo.UserInfoTo;
import com.jerusalem.common.constant.AuthConstant;
import com.jerusalem.common.constant.CartConstant;
import com.jerusalem.common.vo.UserResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/****
 * @Author: jerusalem
 * @Description: CartInterceptor
 * 用户状态拦截器
 * 在执行目标方法之前，判断用户的登录状态，并非封装传递给controller目标请求
 * @Date 2020/9/30 19:20
 *****/
@Component
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    /***
     * 目标方法执行前，进行拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        UserResponseVo user = (UserResponseVo) session.getAttribute(AuthConstant.LOGIN_USER);
        if (user != null){
            //用户已登录
            userInfoTo.setUserId(user.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length>0){
            for (Cookie cookie : cookies) {
                //user-key
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_COOKIE_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }
        //若没有临时用户，要分配一个临时用户
        if (StringUtils.isEmpty(userInfoTo.getUserKey())){
            String userKey = UUID.randomUUID().toString();
            userInfoTo.setUserKey(userKey);
        }
        threadLocal.set(userInfoTo);
        return true;
    }

    /***
     * 业务执行之后，分配临时用户，浏览器保存cookie
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        //如果没有临时用户，一定保存一个临时用户
        if (!userInfoTo.isTempUser()){
            //持续延长临时用户的过期时间
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("tesco.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
