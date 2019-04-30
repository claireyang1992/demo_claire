package com.cmit.codechain.busi.provider.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class MyInterceptor implements HandlerInterceptor {
    public long start=System.currentTimeMillis();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        System.out.println(request.getRequestURI()+ " MyInterceptor Execute preHandle at the time " + new Date());
        start=System.currentTimeMillis();
//        response.sendRedirect("/login");//可以通过拦截器实现重定向
        return true;
    }

    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception{
        System.out.println(request.getRequestURI()+ " MyInterceptor Execute postHandle at the time" + new Date());

    }

    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception{
        System.out.println(request.getRequestURI()+ " MyInterceptor afterCompletion  at the time" +  new Date());

    }
}
