package com.wishes.yearOld.filter;

import com.alibaba.fastjson.JSON;
import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.GroupMemcachedUtil;
import com.wishes.yearOld.common.NetworkUtil;
import com.wishes.yearOld.model.ResponseCode;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.model.User;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-21
 * Time: 下午3:58
 * To change this template use File | Settings | File Templates.
 */
public class UserFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String uriPath = servletRequest.getServletPath();

        MDC.put("logIP", NetworkUtil.getIpAddress(servletRequest));
        MDC.put("logSourceUrl",servletRequest.getRequestURL().toString());
        MDC.put("logSourceType","2");

        if (uriPath.startsWith("/syncapi/")){
            String sqlServerKid = servletRequest.getParameter("Kid");
            if(StringUtils.isEmpty(sqlServerKid)){
                respNoKidError(servletResponse);
                return;
            }
        }
        else if (uriPath.indexOf("_auth") > 0) {
            String passportId = servletRequest.getParameter("passportId");
            if(StringUtils.isEmpty(passportId)){
                passportId = servletRequest.getHeader("passportId");
            }
            if (passportId == null || "".equals(passportId)) {
                respLoginError(servletResponse);
                return;
            }else {
                User user = (User) GroupMemcachedUtil.get("tgod", passportId);
                if (user != null) {
                    request.setAttribute("user", user);
                    MDC.put("userId",user.getId().toString());
                    String userName = user.getNickName();
                    if(StringUtils.isEmpty(userName))
                        userName = user.getMobile();
                    MDC.put("userName",userName);
                } else {
                    respLoginError(servletResponse);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void respLoginError(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED, "请登录")));
        writer.flush();
    }

    private void respNoKidError(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "Kid参数必填")));
        writer.flush();
    }
    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
