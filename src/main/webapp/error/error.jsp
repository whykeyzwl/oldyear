<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Exception"%>
<%@ page import="com.wishes.yearOld.model.Result"%>
<%@ page import="com.wishes.yearOld.model.ResponseCode"%>
<%@ page import="com.alibaba.fastjson.JSON"%>

<%
    Exception e = (Exception)request.getAttribute("exception");
    Result result = Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR);
    if(e != null){
        e.printStackTrace();
        result = Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,e.getClass().getName());
    }
    out.print(JSON.toJSONString(result));
%>
