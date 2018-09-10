package com.dai.interceptor;

import com.dai.pojo.TbUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by DaiHao on 2018-04-20.
 */
public class LoginInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        TbUser root = (TbUser) request.getSession().getAttribute("root");
        TbUser other = (TbUser) request.getSession().getAttribute("other");
        if (root != null && root.getUsername() != null && root.getUsername() != "" ){
            return true;
        }else if (other != null && other.getUsername() != null && other.getUsername() != "" ){
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
