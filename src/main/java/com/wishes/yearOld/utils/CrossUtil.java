package com.wishes.yearOld.utils;

import javax.servlet.http.HttpServletResponse;

public class CrossUtil {

	/*
     * response请求跨域公共设置
     */
    public static HttpServletResponse SetHttpServletResponse(
            HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods",
                "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return response;
    }
}
