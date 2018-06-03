package com.wishes.yearOld.common;



/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2016/9/27
 * Description:
 */
public class CodeGenerator {
    public static String genPassportId(String loginId,byte loginType){
        // 定义生成passportId的算法
        StringBuffer str = new StringBuffer();
        str.append(EncryptUtil.EncryptByMD5(loginId));
        char type = (char)('a'+loginType);
        str.append(type);
        str.append(System.currentTimeMillis());
        return str.toString();
    }

    public static String genSecurityCode(){
        // 定义生成验证码的算法
        Integer code = (int)((Math.random()*9+1)*100000);
        return code.toString();
    }

}
