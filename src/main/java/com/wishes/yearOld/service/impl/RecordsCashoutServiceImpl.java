package com.wishes.yearOld.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.wishes.yearOld.apppay.AppPayConfig;
import com.wishes.yearOld.apppay.AppPayCore;
import com.wishes.yearOld.apppay.HttpProtocolHandler;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.DateUtils;
import com.wishes.yearOld.dao.RecordsCashoutMapper;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.RecordsCashoutService;

import org.apache.commons.httpclient.NameValuePair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-11-23
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
@Service("recordsCashoutService")
public class RecordsCashoutServiceImpl implements RecordsCashoutService {
    @Resource
    RecordsCashoutMapper recordsCashoutMapper;

    /**
     * 转账接口
     * @param recordsCashout
     * @return
     */
    @Override
    public RecordsCashout cashout(RecordsCashout recordsCashout) {

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AppPayConfig.ALIPAY_APP_CHASET);
        Map<String, String> sPara = null;
        try {
            sPara = bliudCashoutParam(recordsCashout);
            //request.setParameters(generatNameValuePair(sPara));
            //request.setMethod("get");
            String signedOrder = AppPayCore.createLinkString(sPara, AppPayConfig.ALIPAY_APP_CHASET);
            String sign = AlipaySignature.rsaSign(sPara, AppPayConfig.ALIPAY_APP_BATCH_TRANS_PRIVATE_KEY, AppPayConfig.ALIPAY_APP_CHASET);
            signedOrder += "&sign=" + URLEncoder.encode(sign, AppPayConfig.ALIPAY_APP_CHASET)+"&sign_type=RSA";
            request.setUrl(AppPayConfig.ALIPAY_APP_ALIPAY_GATEWAY_NEW+"?"+signedOrder);
            //提交转账申请
            HttpResponse response = httpProtocolHandler.execute(request,"","");
            if (response == null) {
                throw new Exception("转账申请无返回值");
            }
            String strResult = response.getStringResultByCharset("gb2312");
            if (strResult.contains("提交成功")) {
                recordsCashout.setBatchStatus(Constant.USER_RECORD_CASHOUT_SUCCESS);
            }else{
                throw new Exception("转账申请提交失败");
            }
        } catch (AlipayApiException e) { //签名异常
            e.printStackTrace();
            recordsCashout.setBatchStatus(Constant.USER_RECORD_CASHOUT_FAIL);
        } catch (Exception ex) { //提交申请出现异常
            try { //去支付宝查询是否提交成功
                HttpRequest request1 = new HttpRequest(HttpResultType.BYTES);
                //设置编码集
                request.setCharset(AppPayConfig.ALIPAY_APP_CHASET);
                Map<String, String> param = new HashMap<>();
                param.put("service", AppPayConfig.ALIPAY_APP_BATCH_QUERY_SERVICE);
                param.put("partner", AppPayConfig.ALIPAY_APP_PID);
                param.put("batch_no", recordsCashout.getBatchNo());
                param.put("_input_charset", AppPayConfig.ALIPAY_APP_CHASET);
                param.put("version", "1.0");
                param.put("timestamp",  DateUtils.format(new Date(),"yyyyMMdd HH:mm:ss"));
                String signedOrder = AppPayCore.createLinkString(param, AppPayConfig.ALIPAY_APP_CHASET);
                String sign = AlipaySignature.rsaSign(param, AppPayConfig.ALIPAY_APP_BATCH_TRANS_PRIVATE_KEY, AppPayConfig.ALIPAY_APP_CHASET);
                signedOrder += "&sign=" + URLEncoder.encode(sign, AppPayConfig.ALIPAY_APP_CHASET)+"&sign_type=RSA";
                request1.setUrl(AppPayConfig.ALIPAY_APP_ALIPAY_GATEWAY_NEW+"?"+signedOrder);
                //request.setParameters(generatNameValuePair(param));
                request1.setMethod("post");
//                request.setUrl(AppPayConfig.ALIPAY_APP_ALIPAY_GATEWAY_NEW);
                HttpResponse response = httpProtocolHandler.execute(request1, "", "");
                if (response == null) {
                    recordsCashout.setBatchStatus(Constant.USER_RECORD_CASHOUT_FAIL);
                } else {
                    String strResult = response.getStringResultByCharset(AppPayConfig.ALIPAY_APP_CHASET);
                    char back = strResult.charAt(strResult.indexOf("</is_success>") - 1);
                    if (String.valueOf(back).equals("F")) {
                        recordsCashout.setBatchStatus(Constant.USER_RECORD_CASHOUT_FAIL);
                    } else if (String.valueOf(back).equals("P")) {
                        recordsCashout.setBatchStatus(Constant.USER_RECORD_CASHOUT_INHAND);
                    }else if (String.valueOf(back).equals("C")) {
                        recordsCashout.setUserStatus(Constant.USER_RECORD_CASHOUT_SUCCESS);
                    } else {
                        recordsCashout.setBatchStatus(Constant.USER_RECORD_CASHOUT_FAIL);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                recordsCashout.setBatchStatus(Constant.USER_RECORD_CASHOUT_FAIL);
            }
        }
        RecordsCashout param = new RecordsCashout();
        param.setId(recordsCashout.getId());
        param.setBatchStatus(recordsCashout.getBatchStatus());
        updateByPrimaryKeySelective(param);
        return recordsCashout;
    }

    @Override
    public int updateByPrimaryKeySelective(RecordsCashout record) {
        return recordsCashoutMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insert(RecordsCashout record) {
        return recordsCashoutMapper.insert(record);
    }

    @Override
    public RecordsCashout selectByBatchNo(String batchNo) {
        return recordsCashoutMapper.selectByBatchNo(batchNo);
    }

    @Override
    public boolean checkNotify(String notify_id) {
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AppPayConfig.ALIPAY_APP_CHASET);
        try {
            Map<String, String> param = new HashMap<>();
            param.put("service", AppPayConfig.ALIPAY_APP_BATCH_NOTIFY_VERIFY);
            param.put("partner", AppPayConfig.ALIPAY_APP_PID);
            param.put("notify_id", notify_id);
            request.setParameters(generatNameValuePair(param));
            request.setUrl(AppPayConfig.ALIPAY_APP_ALIPAY_GATEWAY_NEW);
            //验证
            HttpResponse response = httpProtocolHandler.execute(request, "", "");
            if (response == null) {
                throw new Exception("无返回值");
            }
            String strResult = response.getStringResultByCharset("gb2312");
            if (strResult.equals("true")) {
               return true;
            } else {
                throw new Exception("转账申请提交失败");
            }
        } catch (Exception e) {
             e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateApply(ApplyCashout vo) {
        recordsCashoutMapper.updateApply(vo);
    }

    private static Map<String,String> bliudCashoutParam(RecordsCashout recordsCashout) throws AlipayApiException {
        Map<String, String> map = new HashMap<>();
        map.put("service", AppPayConfig.ALIPAY_APP_BATCH_TRANS_SERVICE);
        map.put("partner", AppPayConfig.ALIPAY_APP_PID);
        map.put("_input_charset", AppPayConfig.ALIPAY_APP_CHASET);
        map.put("notify_url", AppPayConfig.ALIPAY_APP_BATCH_NOTIFY_URL);
        map.put("batch_no", recordsCashout.getBatchNo());
        map.put("account_name", AppPayConfig.ALIPAY_APP_ACCOUNT_NAME);
        map.put("batch_num", "1");
        map.put("batch_fee", recordsCashout.getBatchFee().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"");
        map.put("email", AppPayConfig.ALIPAY_APP_ACCOUNT);
        map.put("pay_date", DateUtils.format(new Date(),"yyyyMMdd"));
        map.put("detail_data", recordsCashout.getUserSerialNumber()+"^"+recordsCashout.getUserAccount()+"^"
                +recordsCashout.getUserName()+"^"+recordsCashout.getBatchFee().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+
                "^id为"+recordsCashout.getUserId()+"转账"+recordsCashout.getBatchFee().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //String sign = AlipaySignature.rsaSign(map, AppPayConfig.ALIPAY_APP_BATCH_TRANS_PRIVATE_KEY, AppPayConfig.ALIPAY_APP_CHASET);
        //map.put("sign_type", AppPayConfig.ALIPAY_APP_SIGN_TYPE);
       // map.put("sign", sign);
        return map;
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
