package com.wishes.yearOld.apppay;

import com.wishes.yearOld.common.PayUtil;
import com.wishes.yearOld.model.HttpRequest;
import com.wishes.yearOld.model.HttpResponse;
import com.wishes.yearOld.model.HttpResultType;
import com.wishes.yearOld.utils.XmlUtil;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.util.Map;

/**
 * Created by tmg-yesky on 2016/10/18.
 */
public class WapPaySubmit {

    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
    private static String buildRequestMysign(Map<String, String> sPara) {
        String prestr = WapPayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(AppPayConfig.WECHAT_SIGN_TYPE.equals("MD5") ) {
            mysign = WapPayCore.sign(prestr, AppPayConfig.WECHAT_PUBLIC_KEY, AppPayConfig.WECHAT_INPUT_CHARSET);
        }
        return mysign;
    }

    /**
     * 生成要请求的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = WapPayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);

        return sPara;
    }

    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取接口的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @param sParaTemp 请求参数数组
     * @return 接口处理结果
     * @throws Exception
     */
    public static String buildRequest(String strParaFileName, String strFilePath,Map<String, String> sParaTemp, String url) throws Exception {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AppPayConfig.WECHAT_INPUT_CHARSET);

        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(url);

        HttpResponse response = httpProtocolHandler.execute(request,strParaFileName,strFilePath);
        if (response == null) {
            return null;
        }

        String strResult = response.getStringResult();

        return strResult;
    }

    /**
     *
     * @param sParaTemp
     * @param url
     * @param isXml 是否将参数转为xml格式 true:转换 false:不转换
     * @return
     * @throws Exception
     */
    public static String buildRequest(Map<String, String> sParaTemp, String url, boolean isXml) throws Exception {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AppPayConfig.WECHAT_INPUT_CHARSET);
        String data = XmlUtil.createXml(sPara);//xml格式数据
        if(isXml){
            RequestEntity entity = new StringRequestEntity(data, "text/plain", AppPayConfig.WECHAT_INPUT_CHARSET);
            request.setEntity(entity);
        }else{
            request.setParameters(generatNameValuePair(sPara));
        }
        request.setUrl(url);

        HttpResponse response = httpProtocolHandler.execute(request,"","");
        if (response == null) {
            return null;
        }

        String strResult = response.getStringResult();

        return strResult;
    }

    /**
    *
    * @param sParaTemp
    * @param url
    * @param isXml 是否将参数转为xml格式 true:转换 false:不转换
    * @return
    * @throws Exception
    */
   public static String buildRequestxcx(Map<String, String> sParaTemp, String url, boolean isXml) throws Exception {
       //待请求参数数组
      // Map<String, String> sPara = buildRequestPara(sParaTemp);
	   // 除去数组中的空值和签名参数
      	Map sPara = PayUtil.paraFilter(sParaTemp);
   	    String prestr = PayUtil.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	String key = "&key=e7144f1a7dd3eac295743e9c1e33bead"; // 商户支付密钥
   	   //MD5运算生成签名
     	String mysign = PayUtil.sign(prestr, key, "utf-8").toUpperCase();
     	sParaTemp.put("sign", mysign);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
       //设置编码集
       request.setCharset(AppPayConfig.WECHAT_INPUT_CHARSET);
       String data = XmlUtil.createXml(sParaTemp);//xml格式数据
       if(isXml){
           RequestEntity entity = new StringRequestEntity(data, "text/plain", AppPayConfig.WECHAT_INPUT_CHARSET);
           request.setEntity(entity);
       }else{
           request.setParameters(generatNameValuePair(sParaTemp));
       }
       request.setUrl(url);

       HttpResponse response = httpProtocolHandler.execute(request,"","");
       if (response == null) {
           return null;
       }

       String strResult = response.getStringResult();

       return strResult;
   }
    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }


}
