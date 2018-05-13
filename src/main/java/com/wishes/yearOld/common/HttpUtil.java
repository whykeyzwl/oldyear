package com.wishes.yearOld.common;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tmg-yesky on 2017/1/12.
 */
public class HttpUtil {

    private static RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setSocketTimeout(5000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .setStaleConnectionCheckEnabled(true)
            .build();

    private static HttpClient createHttpClient(){
        HttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        return client;
    }

    public static HttpResponse postMethod(String url, Map<String, String> param){
        return postMethod(url, param, "UTF-8");
    }

    public static HttpResponse postMethod(String url, Map<String, String> param, String charset){
        HttpResponse response = null;
        try {
            HttpPost post = new HttpPost(url);
            List<NameValuePair> parameters = generatNameValuePair(param);// 定义名值对list，相当于表单上的input对象
            HttpEntity entity = new UrlEncodedFormEntity(parameters, charset);// 把参数放到请求实体
            post.setEntity(entity);// 把请求实体放到post请求中
            HttpClient client = createHttpClient();
            response = sendRequest(client, post);
            //locationUrl = response.getLastHeader("Location").getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse getMethod(String url){
        HttpResponse response = null;
        HttpGet get = new HttpGet(url);
        HttpClient client = createHttpClient();
        response = sendRequest(client, get);
        //locationUrl = response.getLastHeader("Location").getValue();
        return response;
    }

    public static String paseResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();

        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    private static HttpResponse sendRequest(HttpClient client, HttpUriRequest method){
        HttpResponse response = null;
        try {
            response = client.execute(method);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static List<NameValuePair> generatNameValuePair(Map<String, String> properties) {
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nameValuePair;
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

    public static boolean isFromWeixin(HttpServletRequest request){
        String ua =  request.getHeader("user-agent").toLowerCase();
        if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
            return true;
        }
        return false;
    }

    public static boolean isFromWeibo(HttpServletRequest request){
        String ua =  request.getHeader("user-agent").toLowerCase();
        if (ua.indexOf("weibo") > 0) {// 是微博浏览器
            return true;
        }
        return false;
    }
}
