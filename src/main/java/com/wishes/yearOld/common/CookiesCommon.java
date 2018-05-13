package com.wishes.yearOld.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesCommon {
	/**
	 * 设置cookie
	 * @param response
	 * @param name  cookie名字
	 * @param value cookie值
	 * @param maxAge cookie生命周期  以秒为单位
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,int maxAge){
	    Cookie cookie = new Cookie(name,value);
	    cookie.setPath("/");
	    if(maxAge>0)  cookie.setMaxAge(maxAge);
	    response.addCookie(cookie);
	}
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String name){
	    Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie;
	    }else{
	        return null;
	    }  
	}
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static String getCookieByNameValue(HttpServletRequest request,String name){
	    Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie.getValue();
	    }else{
	        return null;
	    }  
	}
	 
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){ 
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
	/**
	 * 取消cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void cancleCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String domain) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		if(domain!=null && !domain.equals("")){
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}
	/**
	 * 删除所有cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void cancleAllCookie(HttpServletRequest request,
			HttpServletResponse response, String domain) {
		Cookie[] cookies=request.getCookies();
		for(int i = 0,len = cookies.length; i < len; i++) {
			Cookie cookie = new Cookie(cookies[i].getName(),null);
			cookie.setMaxAge(0);////立即删除型
			cookie.setPath("/");
			if(domain!=null && !domain.equals("")){
				cookie.setDomain(domain);
			}
			response.addCookie(cookie);
			
			Cookie cookie1 = new Cookie(cookies[i].getName(),null);
			cookie1.setMaxAge(0);////立即删除型
			cookie1.setPath("/");
			cookie1.setDomain("www.htxxpx.com");
			response.addCookie(cookie1);
			
			Cookie cookie2 = new Cookie(cookies[i].getName(),null);
			cookie2.setMaxAge(0);////立即删除型
			cookie2.setPath("/");
			cookie2.setDomain("hxcbs.htxxpx.com");
			response.addCookie(cookie2);
	  }
	}
}
