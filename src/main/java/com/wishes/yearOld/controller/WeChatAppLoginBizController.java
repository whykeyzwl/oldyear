package com.wishes.yearOld.controller;
import java.io.UnsupportedEncodingException;  
import java.security.AlgorithmParameters;  
import java.security.InvalidAlgorithmParameterException;  
import java.security.InvalidKeyException;  
import java.security.Key;  
import java.security.NoSuchAlgorithmException;  
import java.security.NoSuchProviderException;  
import java.security.Security;  
import java.util.Date;  
import java.util.Map;  
  













import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.NoSuchPaddingException;  
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;  
  
 











import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;
import org.apache.xerces.impl.dv.util.Base64;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpMethod;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.stereotype.Component;  
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;  
  













import com.alibaba.fastjson.JSON;  
import com.alibaba.fastjson.JSONObject;  
import com.wishes.yearOld.common.CodeGenerator;
import com.wishes.yearOld.common.HmacUtil;
import com.wishes.yearOld.common.NetworkUtil;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.model.WeChatAppLoginReq;
import com.wishes.yearOld.service.IUserService;


@Controller
@RequestMapping("/weChatAppLogin")
public class WeChatAppLoginBizController {
	 private static final Logger logger = LoggerFactory.getLogger(WeChatAppLoginBizController.class);  
     
	    @Autowired
	    IUserService userService; 
	      
	    String passportIdy="";//用户登录id
	    String openIdsy = "";//微信校验id
	    public static boolean initialized = false;  
	  
	    private static final String APPID = "wxce72954aece9f7d9";  
	    private static final String SECRET = "64b7d69489aceaa87e2232e995e0af03";  
	    @RequestMapping("/getuser.json")
	    @ResponseBody
	    public User login(WeChatAppLoginReq req,HttpServletRequest request)  
	    {  
	    	HttpSession session = request.getSession();
	    	User user=null;
	        //获取 session_key 和 openId  
	        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code="+req.getCode()+"&grant_type=authorization_code";  
	        RestTemplate restTemplate = new RestTemplate();  
	        ResponseEntity<String>  responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);  
	        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)  
	        {  
	            String sessionData = responseEntity.getBody();  
	            logger.info("sessionData = "+ sessionData);  
	            JSONObject jsonObj = JSON.parseObject(sessionData);  
	            String openId = jsonObj.getString("openid");  
	            logger.info("-----openId---------------"+openId);
	            String sessionKey = jsonObj.getString("session_key");  
	            logger.info("-----sessionKey---------------"+sessionKey);
	            String signature = HmacUtil.SHA1(req.getRawData()+sessionKey);  
	            if(!signature.equals(req.getSignature()))  
	            {  
	                logger.info(" req signature="+req.getSignature());  
	                logger.info(" java signature="+req.getSignature());  
	               // throw new SystemException(ResponseMsg.WECHAT_LOGIN_SIGNATURE_ERROR);  
	            }  
	            byte[] resultByte = null;  
	            try {  
	                resultByte = decrypt(Base64.decode(req.getEncryptedData()), Base64.decode(sessionKey), Base64.decode(req.getIv()));  
	            } catch (Exception e) {  
	               // throw new SystemException(ResponseMsg.WECHAT_LOGIN_USER_ERROR);  
	            }  
	                if(null != resultByte && resultByte.length > 0)  
	                {  
	                    String userInfoStr = "";  
	                    try {  
	                        userInfoStr = new String(resultByte, "UTF-8");  
	                    } catch (UnsupportedEncodingException e)   
	                    {  
	                        logger.error(e.getMessage());  
	                    }  
	                    logger.info("userInfo = "+ userInfoStr);  
	                    JSONObject userInfoObj = JSON.parseObject(userInfoStr);  
	                    //判断用户是否注册过
	                    user = userService.findByLoginId(userInfoObj.getString("openid"), (byte) 2);
	                    if(user == null){//未注册,获取用户信息,注册直接登录
	                        //调用get_user_info接口获取用户信息
	                        String nickname = userInfoObj.getString("nickName");
	                        logger.info("nickname:"+nickname);
	                        logger.info("openid:"+userInfoObj.getString("openid"));
	                        String sex = userInfoObj.getString("gender");
	                        logger.info("sex:"+sex);
	                        String face = userInfoObj.getString("headimgurl");
	                        logger.info("face:"+face);
	                        userService.register(userInfoObj.getString("openid"), (byte) 2, face, nickname);//用户注册
	                        logger.info("-----wap端wx登录注册成功---------------");
	                        user = userService.findByLoginId(userInfoObj.getString("openid"), (byte) 2);
	                        logger.info("-----wap端wx登录获取到用户信息----user="+user.toString()+"-----------------");
	                    }
	                    String openid = userInfoObj.getString("openid");
	                    user.setUnionid(userInfoObj.getString("openid"));
	                    user.setLastLoginTime(new Date());
	                    userService.updateAtLogin(user);//更新登录信息
	                    userService.updateAtLoginTimes(user);//更改用户的登录次数
	                    user = userService.findByLoginId(userInfoObj.getString("openid"), (byte) 2);
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
	                   /* MDC.put("logSourceUrl",req.getRequestURL().toString());
	                    MDC.put("logIP", NetworkUtil.getIpAddress(request));*/
	                    MDC.put("logSourceType","5");
	                    
	                    return user;  
	                }else  
	                {  
	                    //throw new SystemException(ResponseMsg.WECHAT_LOGIN_USER_ERROR);  
	                }  
	              
	              
	        }else  
	        {  
	           // throw new SystemException(ResponseMsg.WECHAT_LOGIN_CODE_ERROR);  
	        }
			return user;  
	    }  
	      
	      
	      
	    private byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException {  
	        initialize();  
	        try {  
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");  
	            Key sKeySpec = new SecretKeySpec(keyByte, "AES");  
	  
	            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化   
	            byte[] result = cipher.doFinal(content);  
	            return result;  
	        } catch (NoSuchAlgorithmException e) {  
	            e.printStackTrace();    
	        } catch (NoSuchPaddingException e) {  
	            e.printStackTrace();    
	        } catch (InvalidKeyException e) {  
	            e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	            e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	            e.printStackTrace();  
	        } catch (NoSuchProviderException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (Exception e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	      
	    public static void initialize(){  
	            if (initialized) return;  
	           // Security.addProvider(new BouncyCastleProvider());  
	            initialized = true;  
	    }  
	        //生成iv  
	    public static AlgorithmParameters generateIV(byte[] iv) throws Exception{  
	            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");  
	            params.init(new IvParameterSpec(iv));  
	            return params;  
	    }  
}
