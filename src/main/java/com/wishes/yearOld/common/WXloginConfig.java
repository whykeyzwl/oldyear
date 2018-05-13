package com.wishes.yearOld.common;

/**
 * Created by 17736 on 2016/12/28.
 */
public class WXloginConfig {

    /**
     * 授权类型,固定值
     */
    public static final String RESPONSE_TYPE = "code";

    /**
     * 应用的appid
     */
    public static final String CLIENT_ID_PC = "wxfb0b46b123a1625b";
    public static final String CLIENT_ID_WAP = "wxce72954aece9f7d9";

    /**
     * pc成功授权后的回调地址
     */
    public static final String REDIRECT_URI_PC = "https://www.qingdouke.com/pc/wxlogin.html";

    /**
     * wap成功授权后的回调地址
     */
    public static final String REDIRECT_URI_WAP = "http://47.95.207.69/wap/wxlogin.html";

    /**
     * 请求用户授权时向用户显示的可进行授权的列表
     */
    public static final String GET_USER_INFO_SCOPE = "snsapi_login";

    /**
     * wap请求用户授权时向用户显示的可进行授权的列表
     */
    public static final String GET_USER_INFO_SCOPE_WAP = "snsapi_userinfo";
    public static final String GET_BASE_SCOPE_WAP = "snsapi_base";

    /**
     * pc获取Authorization Code
     */
    public static final String AUTHORIZATION_CODE_API = "https://open.weixin.qq.com/connect/qrconnect";

    /**
     * wap获取Authorization Code
     */
    public static final String AUTHORIZATION_CODE_API_WAP = "https://open.weixin.qq.com/connect/oauth2/authorize";

    /**
     * pc 获取Access Token
     */
    public static final String ACCESS_TOKEN_API_PC = "https://api.weixin.qq.com/sns/oauth2/access_token";

//    /**
//     * wap 获取Access Token
//     */
//    public static final String ACCESS_TOKEN_API_WAP = "https://graph.z.qq.com/moc2/token";
//
//    /**
//     * pc 获取用户OpenID_OAuth2.0
//     */
//    public static final String OPENID_OAUTH_API_PC = "https://graph.qq.com/oauth2.0/me";
//
//    /**
//     * wap 获取用户OpenID_OAuth2.0
//     */
//    public static final String OPENID_OAUTH_API_WAP = "https://graph.z.qq.com/moc2/me";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO_API = "https://api.weixin.qq.com/sns/userinfo";



    /**
     * 授权类型
     */
    public static final String AUTHORIZATION_GRANT_TYPE = "authorization_code";

    /**
     * 应用的appkey
     */
    public static final String CLIENT_SECRET_PC = "44a70c8014c8d33db23cbc85c39e21c5";
    public static final String CLIENT_SECRET_WAP = "64b7d69489aceaa87e2232e995e0af03";




}
