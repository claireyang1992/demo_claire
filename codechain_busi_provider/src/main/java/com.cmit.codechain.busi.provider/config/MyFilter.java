package com.cmit.codechain.busi.provider.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@WebFilter(urlPatterns = "/*", filterName = "MyFilter")
public class MyFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println(((HttpServletRequest) request).getRequestURI() +" MyFilter Execute doFilter begin at the time  " +new Date() );
        chain.doFilter(request,response);
        HttpServletResponse httpResponse=(HttpServletResponse) response;
        System.out.println(((HttpServletRequest) request).getRequestURI() +" MyFilter Execute doFilter begin at the time" +new Date() +" Response HeaderName are "+httpResponse.getHeaderNames());
    }


    public void destroy(){
        System.out.println("MyFilter Execute destroy at the time" +new Date());
    }

}
