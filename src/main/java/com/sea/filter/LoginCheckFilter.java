package com.sea.filter;

import com.alibaba.fastjson.JSON;
import com.sea.common.BaseContext;
import com.sea.common.R;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author 刘海洋
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    AntPathMatcher apm = new AntPathMatcher();
    @Value("${excludePath}")
    String exclupath;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;


        String uri = request.getRequestURI();
        String[] uris = exclupath.split(",");

        if(checkUrl(uris,uri)){
            filterChain.doFilter(request,response);
            return;
        }
//        管理员
        HttpSession session = request.getSession();
        Long employeeId = (Long) session.getAttribute("employee");

        if(employeeId != null){
            BaseContext.setCurrentId(employeeId);
            filterChain.doFilter(request,response);
            return;
        }
//        用户

        Long userId = (Long) session.getAttribute("user");

        if(userId != null){
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }


        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));



    }
    private boolean checkUrl(String[] urls, String requestURI) {

        boolean matchResult = false;
        for (String url : urls) {
            // 匹配 本次请求的requestURI  是否符合 url
            matchResult = apm.match(url, requestURI);

            if (matchResult) {
                return true;
            }
        }
        return false;
    }

}
