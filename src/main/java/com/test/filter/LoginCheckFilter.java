package com.test.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.test.common.BaseContext;
import com.test.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户当前是否已经登陆
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                // 前台登录
                "/user/login"

        };

        // 获取请求 uri
        String uri = request.getRequestURI();

        // 判断本次请求是否需要处理
        boolean flag = check(uri, urls);

        // 不需要处理，放行
        if(flag) {
            filterChain.doFilter(request, response);
            return;
        }

        // 需要处理，判断后台的登陆状态
        if(request.getSession().getAttribute("employee") != null) {
            // ThreadLocal 此线程共用变量
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        // 需要处理，判断前台的登陆状态
        if(request.getSession().getAttribute("user") != null) {
            // ThreadLocal 共用线程变量
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("未登录，访问路径为---->{}", request.getRequestURI());

        // 未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    }

    private boolean check(String requestUri, String[] urls) {
        for (String url : urls) {
            boolean match = ANT_PATH_MATCHER.match(url, requestUri);
            if(match) {
                return true;
            }
        }
        return false;
    }
}
