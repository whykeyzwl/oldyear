package com.wishes.yearOld.apppay;

import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by tmg-yesky on 2016/10/18.
 */
public class WapPayCore {

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串,对value进行编码
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params,String charset) throws UnsupportedEncodingException {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            String encodeValue = URLEncoder.encode(value,charset);
            encodeValue = encodeValue.replace("+", "%20");
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + encodeValue;
            } else {
                prestr = prestr + key + "=" + encodeValue + "&";
            }

        }

        return prestr;
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset)).toUpperCase();
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + "&key=" + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset)).toUpperCase();
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws java.security.SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 产生一个指定长度的的guid,不长于32位
     * @param len
     * @return
     */
    public static String getGuid(int len) {
        if(len>=32){
            len = 32;
        }
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            int k;
            switch ((int) (Math.random() * 2D)) {
                case 0: // '\0'
                    k = (int) (Math.random() * 10D) + 48;
                    break;

                case 1: // '\001'
                    k = (int) (Math.random() * 26D) + 65;
                    break;

                default:
                    k = 95;
                    break;
            }
            sb.append((char) k);
        }

        return sb.toString();
    }

    /**
     * 获取长度10到31位之间的随机字符串
     * @return
     */
    public static String getNonceStr(){
        return getGuid((int) (Math.random() * 22 + 10));
    }

    public static String getDataFromRequest(HttpServletRequest request) {
        BufferedReader br = null;
        InputStreamReader ist = null;
        StringBuilder sb = new StringBuilder();
        try {
            ist = new InputStreamReader(request.getInputStream());
            br = new BufferedReader(ist);
            String line = null;
            while((line = br.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }finally {
            try {
                if(ist != null)
                    ist.close();
                if(br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return sb.toString();
    }

    public static boolean verify(Map<String, String> params, String sign) {
        String text = createLinkString(paraFilter(params));
        return verify(text, sign, AppPayConfig.WECHAT_PUBLIC_KEY, AppPayConfig.WECHAT_INPUT_CHARSET);
    }

    /**
     * 从参数中算出签名sign
     * @param param
     * @return
     */
    public static String getSignFromParams(Map<String, String> param) {

        Map<String, String> sPara = paraFilter(param);
        String prestr = createLinkString(sPara);
        String mysign = sign(prestr, AppPayConfig.WECHAT_PUBLIC_KEY, AppPayConfig.WECHAT_INPUT_CHARSET);

        return mysign;
    }

    public static String toTimeStamp(){
        return String.valueOf(System.currentTimeMillis()).toString().substring(0,10);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Map<String,String> map = new TreeMap<String, String>();
        map.put("appid","wxd930ea5d5a258f4f");
        map.put("mch_id","10000100");
        map.put("device_info","1000");
        map.put("body","test");
        map.put("nonce_str","ibuaiVcKdpRxkhJA");
        String key = "192006250b4c09247ec02edce69f6a2d";
        String stringA = WapPayCore.getSignFromParams(map);
        String sign = WapPayCore.sign(stringA,key,"utf-8").toUpperCase();
        System.out.println(sign);
    }

}

