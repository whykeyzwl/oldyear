<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.wishes.yearOld.model.Result" %>
<%@ page import="com.wishes.yearOld.model.ResponseCode" %>
<%@ page import="com.alibaba.fastjson.JSON" %>

<%
  Result result = Result.BuildFailResult(ResponseCode.SC_METHOD_NOT_ALLOWED);
  out.print(JSON.toJSONString(result));
%>