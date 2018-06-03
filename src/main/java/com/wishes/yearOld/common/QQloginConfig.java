package com.wishes.yearOld.common;

/**
 * Created by 17736 on 2016/12/28.
 */
public class QQloginConfig {

    /**
     * 授权类型,固定值
     */
    public static final String RESPONSE_TYPE = "code";

    /**
     * 应用的appid
     */
    public static final String CLIENT_ID = "101356764";

    /**
     * pc 成功授权后的回调地址
     */
    public static final String REDIRECT_URI_PC = "http://www.qingdouke.com/pc/qqlogin.html";

    /**
     * wap 成功授权后的回调地址
     */
    public static final String REDIRECT_URI_WAP = "http://m.qingdouke.com/wap/qqlogin.html";

    /**
     * 获取Authorization Code
     */
    public static final String AUTHORIZATION_CODE_API = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * pc 获取Access Token
     */
    public static final String ACCESS_TOKEN_API_PC = "https://graph.qq.com/oauth2.0/token";

    /**
     * wap 获取Access Token
     */
    public static final String ACCESS_TOKEN_API_WAP = "https://graph.z.qq.com/moc2/token";

    /**
     * pc 获取用户OpenID_OAuth2.0
     */
    public static final String OPENID_OAUTH_API_PC = "https://graph.qq.com/oauth2.0/me";

    /**
     * wap 获取用户OpenID_OAuth2.0
     */
    public static final String OPENID_OAUTH_API_WAP = "https://graph.z.qq.com/moc2/me";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO_API = "https://graph.qq.com/user/get_user_info";

    /**
     * 请求用户授权时向用户显示的可进行授权的列表
     */
    public static final String GET_USER_INFO_SCOPE = "get_user_info";

    /**
     * 授权类型
     */
    public static final String AUTHORIZATION_GRANT_TYPE = "authorization_code";
    public static final String REFRESH_GRANT_TYPE = "refresh_token";

    /**
     * 应用的appkey
     */
    public static final String CLIENT_SECRET = "30ab6de0a3e560ef72fb00585124f6e2";




}
