package com.wishes.yearOld.common;

/**
 * Created by 17736 on 2016/12/28.
 */
public class WBloginConfig {

    /**
     * 授权类型,固定值
     */
    public static final String RESPONSE_TYPE = "code";

    /**
     * 应用的appid
     */
    public static final String CLIENT_ID = "4232693628";

    /**
     * pc成功授权后的回调地址
     */
    public static final String REDIRECT_URI_PC = "http://www.qingdouke.com/pc/wblogin.html";

    /**
     * wap成功授权后的回调地址
     */
    public static final String REDIRECT_URI_WAP = "http://m.qingdouke.com/wap/wblogin.html";

    /**
     * 请求用户授权时向用户显示的可进行授权的列表
     */
    public static final String GET_USER_INFO_SCOPE = "statuses_to_me_read";

    /**
     * 请求用户授权时向用户显示的可进行授权的列表
     */
    public static final String WEIBO_CONNECT_DISPLAY_WAP = "mobile";

    /**
     * 获取Authorization Code
     */
    public static final String AUTHORIZATION_CODE_API = "https://api.weibo.com/oauth2/authorize";

    /**
     * pc 获取Access Token
     */
    public static final String ACCESS_TOKEN_API_PC = "https://api.weibo.com/oauth2/access_token";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO_API = "https://api.weibo.com/2/users/show.json";


    /**
     * 授权类型
     */
    public static final String ACCESS_TOKEN_GRANT_TYPE = "authorization_code";

    /**
     * 应用的appkey
     */
    public static final String CLIENT_SECRET = "083e7934e1116eb55b08c2662657b31c";




}
