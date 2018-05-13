package com.wishes.yearOld.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.wishes.yearOld.common.CodeGenerator;
import com.wishes.yearOld.common.HttpUtil;
import com.wishes.yearOld.common.NetworkUtil;
import com.wishes.yearOld.common.QQloginConfig;
import com.wishes.yearOld.common.StringUtils;
import com.wishes.yearOld.common.WBloginConfig;
import com.wishes.yearOld.common.WXloginConfig;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.service.IUserService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.naming.NoInitialContextException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 17736 on 2016/12/28.
 */

@Controller
@RequestMapping
public class ThirdLoginController {

    private static final Logger dblog = Logger.getLogger("dblog");

    @Autowired
    IUserService userService;

    private Log logger = LogFactory.getLog(getClass());
    String passportIdy="";//用户登录id
    String openIdsy = "";//微信校验id
    @RequestMapping("/wap/qqtologin.html")
    public ModelAndView qqLogin(HttpServletRequest request,String reurl){
        String locationUrl = null;
        String state = CodeGenerator.genSecurityCode();
        request.getSession().setAttribute("state",state);
        try {
            locationUrl = QQloginConfig.AUTHORIZATION_CODE_API
                    +  "?response_type="+QQloginConfig.RESPONSE_TYPE
                    +  "&client_id="+ QQloginConfig.CLIENT_ID
                    +  "&redirect_uri="+ URLEncoder.encode(QQloginConfig.REDIRECT_URI_WAP+"?reurl="+reurl,"utf-8")
                    +  "&state="+ state
                    +  "&g_ut="+ 2
                    +  "&scope="+ QQloginConfig.GET_USER_INFO_SCOPE;
            logger.info("---wap端qq登录----locationUrl="+locationUrl+"-----------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView(locationUrl));
    }

    @RequestMapping("/wap/qqlogin.html")
    public ModelAndView getWapLoginAccessToken(HttpServletRequest request,HttpServletResponse res){
        String reurl = request.getParameter("reurl");
        logger.info("-----wap-qq登录-重定向地址reurl="+reurl+"-------");
        HttpSession session = request.getSession();
        String state = request.getParameter("state");
        logger.info("------wap-qq登录-获取AccessToken返回state=" + state + "--------------------");
        String state_session = (String)session.getAttribute("state");
        logger.info("------wap-qq登录-获取AccessToken返回state_session="+state_session+"--------------------");
        session.removeAttribute("state");
        if(state == null){
            logger.info("---wap端qq登录----获取AccessToken返回state为null--------------------");
            return new ModelAndView("/wap/login.html");
        }
        if(state != null && !state.equals(state_session)){
            logger.info("---wap端qq登录----获取AccessToken返回state不匹配--------------------");
            return new ModelAndView("/wap/login.html");
        }

        String usercancel = request.getParameter("usercancel");
        if(usercancel != null && Integer.valueOf(usercancel) != 0){
            logger.info("-------wap-取消qq登录:usercancel="+usercancel+"--------------------");
            //取消登录
            return new ModelAndView(new RedirectView(StringUtils.trimToEmpty(reurl)));
        }

        String code = request.getParameter("code");
        logger.info("-------wap端qq登录获取到Authorization Code="+code+"--------------------");

        try {
            String access_token_url = QQloginConfig.ACCESS_TOKEN_API_PC
                    + "?grant_type="+QQloginConfig.AUTHORIZATION_GRANT_TYPE
                    + "&client_id="+QQloginConfig.CLIENT_ID
                    + "&client_secret="+QQloginConfig.CLIENT_SECRET
                    + "&code="+code
                    + "&redirect_uri="+URLEncoder.encode(QQloginConfig.REDIRECT_URI_WAP,"utf-8");
            logger.info("---------wap端qq登录-access_token_url---------"+ access_token_url+"-----------");
            HttpResponse response = HttpUtil.getMethod(access_token_url);
            String response_str = EntityUtils.toString(((org.apache.http.HttpResponse) response).getEntity());
            logger.info("---------wap端qq登录-获取access_token的response---------"+ response_str+"-----------");
            //todo 需调试确定 access_token
            String access_token = response_str.split("\\&")[0].split("=")[1];
            logger.info("-------wap端qq登录获取-access_token="+access_token+"--------------------");
            if(access_token != null){//获取OpenId
                HttpResponse response_openid = (HttpResponse) HttpUtil.getMethod(QQloginConfig.OPENID_OAUTH_API_PC+"?access_token="+access_token);
                String regexStr = "\"openid\":\"(.*)\"" ;
                Pattern p = Pattern.compile(regexStr);
                Matcher m = p.matcher(EntityUtils.toString(((org.apache.http.HttpResponse) response_openid).getEntity()));
                String openid = null;
                while (m.find()) {
                    openid = m.group(1);
                }//登录用户qq唯一标识

                logger.info("----------wap端qq登录获取用户openid="+openid+"----------");
                //判断用户是否是会员
                User user = userService.findByLoginId(openid, (byte) 1);
                if(user == null){
                    //调用get_user_info接口获取用户信息
                    String user_info_url = QQloginConfig.GET_USER_INFO_API
                            +"?access_token="+access_token
                            +"&oauth_consumer_key="+QQloginConfig.CLIENT_ID
                            +"&openid="+openid;
                    logger.info("-----wap端qq登录-获取用户信息-user_info_url="+user_info_url+"--------------");
                    HttpResponse response_user_info = (HttpResponse) HttpUtil.getMethod(user_info_url);
                    if(response_user_info == null){
                        throw new Exception("-----wap端qq登录获取用户信息响应失败-----------");
                    }
                    JSONObject user_json = JSON.parseObject(EntityUtils.toString(response_user_info.getEntity()));
                    logger.info("-----wap端qq登录获取到用户信息----response_user_info="+user_json.toJSONString()+"-----------------");
                    int ret = user_json.getIntValue("ret");
                    if(ret == 0){
                        //获取用户基本信息
                        String nickname = user_json.getString("nickname");
                        String face = user_json.getString("figureurl_qq_1");
                        String sex = user_json.getString("gender");
                        userService.register(openid,(byte)1,face,nickname);//用户注册
                        logger.info("----wap端qq注册成功---nickname="+ nickname+"---------------");
                        user = userService.findByLoginId(openid, (byte) 1);
                    }else{
                        logger.info("-----ret="+ret+"----msg="+user_json.getString("msg")+"------------");
                        throw new Exception("------------wap端qq登录用户返回码错误-------------");
                    }
                }
                user.setUnionid(openid);
                user.setLastLoginTime(new Date());
                userService.updateAtLogin(user);//更新登录信息
                userService.updateAtLoginTimes(user);//更改用户的登录次数
                user = userService.findByLoginId(openid, (byte) 1);
                
                String passportId = CodeGenerator.genPassportId(user.getLoginID(),user.getLoginType());
                //将passportId与user信息存入缓存
                userService.syncUserToCache(passportId, user);
                passportIdy=passportId;
                user.setPassportId(passportId);
                session.setAttribute("user", user);//把用户放入session
                MDC.put("userId",user.getId().toString());
                String userName = user.getNickName();
                MDC.put("userName",userName);
                MDC.put("logActionType","1");
                MDC.put("logSourceUrl",request.getRequestURL().toString());
                MDC.put("logIP", NetworkUtil.getIpAddress(request));
                MDC.put("logSourceType","5");
                dblog.info("用户"+userName+"从wap端通过QQ登录成功");
                logger.info("----wap端qq登录成功---nickname="+user.getNickName()+"---------------");
            }else{
                throw new Exception("--------获取access_token失败--------");
            }
            logger.info("----获取access_token成功---access_token="+access_token+"---------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView(reurl+"?passportId="+passportIdy));
    }

    @RequestMapping("/wap/wbtologin.html")
    public ModelAndView wbLoginWap(HttpServletRequest request,String reurl){
        String locationUrl = null;
        String state = CodeGenerator.genSecurityCode();
        try {
            locationUrl = WBloginConfig.AUTHORIZATION_CODE_API
                    +"?client_id="+ WBloginConfig.CLIENT_ID
                    +"&redirect_uri="+ URLEncoder.encode(WBloginConfig.REDIRECT_URI_WAP+"?reurl="+reurl,"utf-8")
                    +"&scope="+WBloginConfig.GET_USER_INFO_SCOPE
                    +"&state="+state;
            logger.info("------------wap-微博登录--locationUrl="+locationUrl+"-------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView(locationUrl));
    }

    @RequestMapping("/wap/wblogin.html")
    public ModelAndView getWapcWBLoginAccessToken(HttpServletRequest request){
        HttpSession session = request.getSession();
        String state = request.getParameter("state");
        logger.info("------wap-微博登录-获取AccessToken返回state="+state+"--------------------");
        String reurl = request.getParameter("reurl");
        logger.info("------wap-微博登录-重定向地址reurl="+reurl+"---------------------------");
        session.removeAttribute("state");
        if(state == null){
            logger.info("------wap-微博登录-获取AccessToken返回state为null--------------------");
            return new ModelAndView(new RedirectView(reurl));
        }
        String code = request.getParameter("code");
        if(StringUtils.isEmpty(code)){
            logger.info("------wap-微博登录-获取AccessToken返回code为null,用户取消授权--------------------");
            return new ModelAndView(new RedirectView(reurl));
        }
        logger.info("-------wap-微博登录获取到Authorization Code="+code+"--------------------");

        Map<String, String> param = new HashMap<>();
        param.put("client_id", WBloginConfig.CLIENT_ID);
        param.put("client_secret ", WBloginConfig.CLIENT_SECRET);
        param.put("grant_type ", WBloginConfig.ACCESS_TOKEN_GRANT_TYPE);
        //todo wap端wb登录回调接口
        param.put("code", code);
        param.put("redirect_uri", WBloginConfig.REDIRECT_URI_WAP);
        try {
            String access_token_url = WBloginConfig.ACCESS_TOKEN_API_PC
                    + "?client_id="+WBloginConfig.CLIENT_ID
                    + "&client_secret="+WBloginConfig.CLIENT_SECRET
                    + "&grant_type="+WBloginConfig.ACCESS_TOKEN_GRANT_TYPE
                    + "&code="+code
                    + "&redirect_uri="+URLEncoder.encode(WBloginConfig.REDIRECT_URI_WAP+"?reurl="+reurl,"utf-8");
            HttpResponse response = (HttpResponse) HttpUtil.postMethod(access_token_url, param);
            String access_token_res_str = EntityUtils.toString(((org.apache.http.HttpResponse) response).getEntity());
            logger.info("----------wap-微博登录获取access_token和openid的response---------"+ access_token_res_str+"-----------");
            //todo 需调试确定 access_token
            JSONObject token_json = JSON.parseObject(access_token_res_str);
            String access_token = token_json.getString("access_token");
            String uid = token_json.getString("uid");
            if(StringUtils.isEmpty(access_token_res_str) || StringUtils.isEmpty(uid)){
                throw new Exception("--------wb pc登录获取access_token异常---------");
            }
            logger.info("----wap-微博登录获取的access_token="+access_token+"----uid="+uid+"------------");
            session.setAttribute("uid",uid);
            //判断用户是否注册过
            User user = userService.findByLoginId(uid, (byte) 3);
            if(user == null){//未注册,获取用户信息,注册直接登录
                //调用get_user_info接口获取用户信息
                String user_info_url = WBloginConfig.GET_USER_INFO_API
                        +"?access_token="+access_token
                        +"&uid="+uid;
                HttpResponse response_user_info = (HttpResponse) HttpUtil.getMethod(user_info_url);
                if(response_user_info == null){
                    return new ModelAndView("redirect:/pc/login.html");
                }
                JSONObject user_json = JSON.parseObject(EntityUtils.toString(((org.apache.http.HttpResponse) response_user_info).getEntity()));

                logger.info("-----wap-微博获取到用户信息----response_user_info="+user_json.toJSONString()+"-----------------");
                String nickname = user_json.getString("screen_name");
                //Integer count = userService.selectUserCountByNickname(nickname);
                //if(count>0){
                //    nickname += DateUtils.format(new Date(),"yyyyMMddHHmmss");
               // }
                String gender = user_json.getString("gender");
                String sex = "m".equals(gender)?"男":"女";
                String face = user_json.getString("profile_image_url");
                userService.register(uid, (byte) 3, face, nickname);//用户注册
               
                user = userService.findByLoginId(uid, (byte) 3);
                logger.info("------wap-微博获取用户注册完毕user="+user.toString()+"----------------");
            }
            user.setUnionid(uid);
            user.setLastLoginTime(new Date());
            userService.updateAtLogin(user);//更新登录信息
            userService.updateAtLoginTimes(user);//更改用户的登录次数
            user = userService.findByLoginId(uid, (byte) 3);
            
            String passportId = CodeGenerator.genPassportId(user.getLoginID(),user.getLoginType());
            logger.info("passportIdy======================="+passportId);
            //将passportId与user信息存入缓存
            userService.syncUserToCache(passportId, user);
            passportIdy=passportId;
            user.setPassportId(passportId);
            session.setAttribute("user", user);//把用户放入session
            session.setMaxInactiveInterval(1800);
            MDC.put("userId",user.getId().toString());
            String userName = user.getNickName();
            MDC.put("userName",userName);
            MDC.put("logActionType","1");
            MDC.put("logSourceUrl",request.getRequestURL().toString());
            MDC.put("logIP", NetworkUtil.getIpAddress(request));
            MDC.put("logSourceType","5");
            dblog.info("用户"+userName+"从wap端通过微博登录成功");
            logger.info("----wap-微博用户登录成功---nickname="+user.getNickName()+"---------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView(reurl+"?passportId="+passportIdy));
    }
   
    @RequestMapping("/wap/wxtologin.html")
    public String wxLoginWap(HttpServletRequest request){
        String locationUrl = null;
        String state = CodeGenerator.genSecurityCode();
        request.getSession().setAttribute("state",state);
        String reurl = request.getParameter("reurl");
        //openIdsy =  "";
        //passportIdy =  "";
        try {
            locationUrl = WXloginConfig.AUTHORIZATION_CODE_API_WAP
                    +"?appid="+WXloginConfig.CLIENT_ID_WAP
                    +"&redirect_uri="+URLEncoder.encode(WXloginConfig.REDIRECT_URI_WAP+"?reurl="+reurl,"utf-8")
                    +"&response_type="+WXloginConfig.RESPONSE_TYPE
                    +"&scope="+WXloginConfig.GET_USER_INFO_SCOPE_WAP
                    +"&state="+state+"#wechat_redirect";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info("----wap wx登录获取code url="+locationUrl+"-----------------------");
        return "redirect:"+locationUrl;
    }

    @RequestMapping("/wap/wxlogin.html")
    public ModelAndView getWapWXLoginAccessToken(HttpServletRequest request,HttpServletResponse res){
        HttpSession session = request.getSession();
        String state = request.getParameter("state");
        String state_session = (String)session.getAttribute("state");
        String reurl = request.getParameter("reurl");
        logger.info("-----wap端wx登录完成跳转地址reurl="+reurl+"----------");
        session.removeAttribute("state");
        if(state == null){
            logger.info("-------获取AccessToken返回state为null--------------------");
            //return new ModelAndView("/wap/login.html");
        }
        String code = request.getParameter("code");
        if(StringUtils.isEmpty(code)){
            logger.info("-------获取AccessToken返回code为null,用户取消授权--------------------");
            //return new ModelAndView("/wap/login.html");
        }
        logger.info("-------wap-wx登录获取到Authoriz ation Code--------------------");

        String access_token_url = "";
        access_token_url += WXloginConfig.ACCESS_TOKEN_API_PC
                +"?appid="+WXloginConfig.CLIENT_ID_WAP
                +"&secret="+WXloginConfig.CLIENT_SECRET_WAP
                +"&code="+code
                +"&grant_type="+WXloginConfig.AUTHORIZATION_GRANT_TYPE;
        String openid="";
        try {
            // 创建http请求客户端
            HttpResponse response = HttpUtil.getMethod(access_token_url);
            String response_str = EntityUtils.toString(response.getEntity());
            logger.info("----------wap端wx登录获取access_token和openid的response---------"+response_str+"-----------");
            //todo 需调试确定 access_token
            JSONObject token_json = JSON.parseObject(response_str);
            String errcode = token_json.getString("errcode");
            String errmsg = token_json.getString("errmsg");
            if(errcode != null){
                logger.info("-------wap端wx登录获取access_token返回错误errcode="+errcode+"errmsg="+errmsg);
            }
            String access_token = token_json.getString("access_token");
            openid = token_json.getString("openid");
//            Cookie cookie = new Cookie(Constant.WECHAT_OPEN_ID_KEY, openid);
//            res.addCookie(cookie);
            session.setAttribute("openId", openid);
            openIdsy =  openid;
            logger.info("微信OAuth2.0回调-将opendid添加到用户session中，openid:"+openid);
            logger.info("------wap端wx登录获取 access_token="+access_token+"-----openid="+openid+"--------------------");
            if(StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid)){
                throw new Exception("-----wap端wx登录获取access_token和openid错误----------------------------");
            }
            String user_info_url = WXloginConfig.GET_USER_INFO_API
                    +"?access_token="+access_token
                    +"&openid="+openid
                    +"lang=zh_CN";
            HttpResponse response_user_info = HttpUtil.getMethod(user_info_url);
            JSONObject user_json = JSON.parseObject(EntityUtils.toString(response_user_info.getEntity(),"utf-8"));
            String user_errcode = token_json.getString("errcode");
            String user_errmsg = token_json.getString("errmsg");
            if(user_errcode != null){
                logger.info("---wap端wx登录-errcode="+user_errcode+"----errmsg="+user_errmsg+"----------");
                throw new Exception("----------wap端wx登录获取用户信息失败---------------");
            }
            logger.info("-----wap端wx登录获取到用户信息----response_user_info="+user_json.toJSONString()+"-----------------");
            String unionid = user_json.getString("unionid");
            logger.info("-----wap端wx登录获取到用户信息----unionid="+unionid+"-----------------");
            //判断用户是否注册过
            User user = userService.findByLoginId(unionid, (byte) 2);
            if(user == null){//未注册,获取用户信息,注册直接登录
                //调用get_user_info接口获取用户信息
                String nickname = user_json.getString("nickname");
               /* Integer count = userService.selectUserCountByNickname(nickname);
                if(count>0){
                    nickname += DateUtils.format(new Date(),"yyyyMMddHHmmss");
                }*/
                String sex = user_json.getIntValue("sex")==1?"男":"女";
                String face = user_json.getString("headimgurl");
                userService.register(unionid, (byte) 2, face, nickname);//用户注册
                logger.info("-----wap端wx登录注册成功---------------");
                user = userService.findByLoginId(unionid, (byte) 2);
                logger.info("-----wap端wx登录获取到用户信息----user="+user.toString()+"-----------------");
            }
            user.setUnionid(unionid);
            user.setLastLoginTime(new Date());
            userService.updateAtLogin(user);//更新登录信息
            userService.updateAtLoginTimes(user);//更改用户的登录次数
            user = userService.findByLoginId(unionid, (byte) 2);
            user.setOpenId(openid);
            
            String passportId = CodeGenerator.genPassportId(user.getLoginID(),user.getLoginType());
            logger.info("passportIdy======================="+passportId);
            //将passportId与user信息存入缓存
            userService.syncUserToCache(passportId, user);
            user.setPassportId(passportId);
            session.setAttribute("user", user);//把用户放入session
            logger.info("sessionuser======================="+session.getAttribute("user"));
            passportIdy=passportId;
            session.setMaxInactiveInterval(1800);
            MDC.put("userId",user.getId().toString());
            String userName = user.getNickName();
            MDC.put("userName",userName);
            MDC.put("logActionType","1");
            MDC.put("logSourceUrl",request.getRequestURL().toString());
            MDC.put("logIP", NetworkUtil.getIpAddress(request));
            MDC.put("logSourceType","5");
            dblog.info("用户"+userName+"从wap端通过微信登录成功");
            logger.info("----wap用户登录成功---nickname="+user.getNickName()+"---------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView(reurl));
    }
  
    /**
     * 获取当前用户
     * @param request
     * @param 
     * @return
     */
    @RequestMapping(value = "/wap/getUser.json", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  User postOrderList(HttpServletRequest request) {
    	//String passportIds = request.getParameter("passportId");
    	HttpSession session = request.getSession();
    	User _user = (User) session.getAttribute("user");
        return  _user;
    }
    
    /**
     * 获取当前用户openId
     * @param request
     * @param 
     * @return
     */
    @RequestMapping(value = "/wap/getOpenId.json", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  Object getOpenId(HttpServletRequest request) {
    	//HttpSession session = request.getSession();
    	//String openIds = (String) session.getAttribute("openId");
    	String  openIds = openIdsy;
    	
        return  openIds;
        
    }
}
