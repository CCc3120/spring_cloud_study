package com.bingo.test.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author h-bingo
 * @Date 2023-05-05 17:39
 * @Version 1.0
 */
@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest request1 = (HttpServletRequest) request;


        // System.out.println(request.getContentType());

        chain.doFilter(request, response);
    }
}
