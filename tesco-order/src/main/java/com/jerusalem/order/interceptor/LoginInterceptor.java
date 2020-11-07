package com.jerusalem.order.interceptor;

import com.jerusalem.common.constant.AuthConstant;
import com.jerusalem.common.vo.UserResponseVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/****
 * @Author: jerusalem
 * @Description: LoginInterceptor
 * 订单登录拦截器
 * @Date 2020/11/7 12:29
 *****/
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /***
     * 全系统共享当前登录用户
     */
    public static ThreadLocal<UserResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserResponseVo userResponseVo = (UserResponseVo) request.getSession().getAttribute(AuthConstant.LOGIN_USER);
        if (userResponseVo != null){
            //全系统共享
            loginUser.set(userResponseVo);
            return true;
        }else {
            //没登陆，拦截,重定向到登录页面
            request.getSession().setAttribute("msg","请先登录！");
            response.sendRedirect("http://auth.tesco.com/login.html");
            return false;
        }
    }
}
