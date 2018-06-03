package com.wishes.yearOld.common;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Set;


public class SendSmsUtil {

	private static final Logger logger = Logger.getLogger(SendSmsUtil.class);

	private static final String SERVER_URL_PRODUCT = "app.cloopen.com";
	private static final String SERVER_URL_SANDBOX = "sandboxapp.cloopen.com";
	private static final String SERVER_PORT = "8883";

	private static final String ACCOUNT_SID = "8a216da85751104901576f800fe01183";
	private static final String AUTH_TOKEN = "3ce1e1fc0147417d8b70494e6e9761e7";
	private static final String APP_ID = "8a216da85751104901576f8011391187";



	public static void sendSmsForLogin(String mobile,String securityCode){
		sendTemplateSMS(mobile, "121471",new String[]{securityCode});
	}

	public static void sendSmsForRegister(String mobile,String securityCode){
		sendTemplateSMS(mobile, "121472",new String[]{securityCode});
	}

	public static void sendSmsForChangeMobile(String mobile,String securityCode){
		sendTemplateSMS(mobile, "121473",new String[]{securityCode});
	}

	public static void sendSmsForFindPassword(String mobile,String securityCode){
		sendTemplateSMS(mobile, "121475",new String[]{securityCode});
	}

	public static void sendSmsForCashount(String mobile,String securityCode){
		sendTemplateSMS(mobile, "121476",new String[]{securityCode});
	}

	public static void sendSmsForProfit(String mobile,String UserFrom){
		sendTemplateSMS(mobile, "121477",new String[]{UserFrom});
	}

	public static void sendSmsForApply(String mobile,String actitvy){
		sendTemplateSMS(mobile, "121478",new String[]{actitvy});
	}

	private static void sendTemplateSMS(String mobile, String tempId,String[] content) {
		HashMap<String, Object> result = null;

		//初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
		
		//******************************注释*********************************************
		//*初始化服务器地址和端口                                                       *
		//*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		//*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
		//*******************************************************************************
		restAPI.init(SERVER_URL_PRODUCT, SERVER_PORT);
		
		//******************************注释*********************************************
		//*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
		//*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
		//*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
		//*******************************************************************************
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		
		
		//******************************注释*********************************************
		//*初始化应用ID                                                                 *
		//*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
		//*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
		//*******************************************************************************
		restAPI.setAppId(APP_ID);
		
		
		//******************************注释****************************************************************
		//*调用发送模板短信的接口发送短信                                                                  *
		//*参数顺序说明：                                                                                  *
		//*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
		//*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
		//*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		//*第三个参数是要替换的内容数组。																														       *
		//**************************************************************************************************
		
		//**************************************举例说明***********************************************************************
		//*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
		//*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
		//*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
		//*********************************************************************************************************************
		result = restAPI.sendTemplateSMS(mobile,tempId ,content);
		
		logger.debug("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			logger.info("短信发送成功");
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				logger.debug(key +" = "+object);
			}
		}else{
			//异常返回输出错误码和错误信息
			logger.error("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}
	}

}
