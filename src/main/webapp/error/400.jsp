<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.wishes.yearOld.model.Result" %>
<%@ page import="com.wishes.yearOld.model.ResponseCode" %>
<%@ page import="com.alibaba.fastjson.JSON" %>

<%
  Result result = Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "缺少必要参数");
  out.print(JSON.toJSONString(result));
%>