package com.wishes.yearOld.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.wishes.yearOld.Exception.PaymentAliRefundException;
import com.wishes.yearOld.apppay.AppPayConfig;
import com.wishes.yearOld.apppay.AppPayCore;
import com.wishes.yearOld.apppay.AppPaySubmit;
import com.wishes.yearOld.apppay.OrderNum;
import com.wishes.yearOld.common.*;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.*;
import com.wishes.yearOld.utils.XmlUtil;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-10-20
 * Time: 下午9:35
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/payment")
public class PaymentController {

    /**
     * 日志实例
     */
    private static final Logger logger = Logger.getLogger(PaymentController.class);

    @Autowired
    PhotoAlbumService photoAlbumService;

    @Autowired
    ActivityService activityService;

    @Autowired
    OrderService orderService;

    @Autowired

    SalesRecordService salesRecordService;

    @Autowired
    RecordsCashoutService recordsCashoutService;

    @Autowired
    private IUserService userService;
    
    @Autowired
    private VipService vipService;

    @RequestMapping(value = "/post_alipay_trans_cashout_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public
    @ResponseBody
    Result alipayTransCashout(HttpServletRequest request, @RequestParam(value = "amount", defaultValue = "0") BigDecimal amount) {
        //获取当前用户
        User user = (User)request.getAttribute("user");
        if (amount.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO)==0) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "金额不能为0");
        }
        RecordsCashout recordsCashout = new RecordsCashout();
        recordsCashout.setBatchNo(OrderNum.getBatchNum());
        recordsCashout.setCreateTime(new Date());
        recordsCashout.setBatchStatus(Constant.USER_RECORD_CASHOUT_INIT);
        recordsCashout.setBatchFee(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        recordsCashout.setUserId(user.getId());
       // recordsCashout.setUserAccount(user.getZhifubao());
        recordsCashout.setUserAccount("18510116204");
        //recordsCashout.setUserName(user.getRealName());
        recordsCashout.setUserName("孙伟");
        recordsCashout.setUserStatus(Constant.USER_RECORD_CASHOUT_USER_INIT);
        recordsCashout.setUserSerialNumber(OrderNum.getBatchUserNum());
        recordsCashoutService.insert(recordsCashout);
        return  Result.BuildSuccessResult(recordsCashoutService.cashout(recordsCashout));
    }

    @RequestMapping(value = "/post_payment_notify_trans", method = RequestMethod.POST, produces = "application/text;charset=UTF-8")
    public
    @ResponseBody
    String paymentNotifyTrans(HttpServletRequest request) {
        Map<String,Object> paramsMap = request.getParameterMap();
        Map<String, String> params = convertParamsMap(paramsMap);
        logger.info("===============转账异步通知===========");
        for (String key : params.keySet()) {
            logger.info(key+" : "+params.get(key));
        }
        //异步通知校验id
        String notify_id = params.get("notify_id");
        if (!recordsCashoutService.checkNotify(notify_id)) {
            return "false";
        }
        //批次id
        String batch_no = params.get("batch_no");
        //批量转账
        String success_details = params.get("success_details");
        String fail_details = params.get("fail_details");
        if(!StringUtils.isEmpty(success_details)){
            String[] details_s = success_details.split("\\|");
            for(String ds : details_s){
                ApplyCashout vo = new ApplyCashout();
                String[] data = ds.split("\\^");
                vo.setFinishTime(DateUtils.parse(data[7], "yyyyMMddHHmmss"));
                vo.setPayStatus((byte)3);
                vo.setBatchNo(batch_no);
                vo.setAccountNum(data[0]);
                recordsCashoutService.updateApply(vo);
            }
        }
        if(!StringUtils.isEmpty(fail_details)){
            String[] details_f = fail_details.split("\\|");
            for(String df : details_f){
                ApplyCashout vo = new ApplyCashout();
                String[] data = df.split("\\^");
                vo.setFinishTime(DateUtils.parse(data[7],"yyyyMMddHHmmss"));
                vo.setBatchNo(batch_no);
                vo.setAccountNum(data[0]);
                vo.setFailReason(data[5]);
                vo.setPayStatus((byte)4);
                recordsCashoutService.updateApply(vo);
            }
        }
        return "success";
    }



    /**
     * alipay_提交订单(包括购买图集，购买活动规则)_返回签名后的订单信息
     * @param request
     * @return
     */
      @RequestMapping(value = "/post_alipay_apply_order_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
      @ResponseBody
      public Result alipayApplyOrder(HttpServletRequest request,GoodsVO goodsVO) {
        //获取当前用户
        //User user = (User)request.getAttribute("user");
        String passportId = request.getParameter("passportId");
        User user = null;
        if(!StringUtils.isTrimEmpty(passportId)){
            user = userService.loadUserFromCache(passportId);
        }
        if(user == null){
            return Result.BuildFailResult(ResponseCode.SC_FORBIDDEN,"未登录");
        }
        String type = request.getParameter("type");//得到付款类型1.VIP解锁2.图集下载3.图集解锁
    	
    	String monthcout = request.getParameter("monthcout");//会员月数
        //校验商品是否存在并返回价钱，返回0则不存在该商品
        Map<String,Object> map = validateAndReturnPrice(goodsVO);
        String msg = map.get("msg").toString();
        BigDecimal total_amount = null;
        Integer _have = orderService.selectHaveBuy(user.getId());//是否首次购买
        if(_have == 0){//首次购买只需一元
            total_amount = new BigDecimal(1.00);
        }else{
            total_amount = (BigDecimal) map.get("price");
        }
        if(monthcout!="" && monthcout!="null" && monthcout!="undefined" ){
        	Vip vip = new Vip();
        	vip.setMonthCount(Integer.parseInt(monthcout));		
        	List<Vip> viplst = vipService.selectVip(vip);
        	if(viplst!=null && viplst.size()>0){
        	total_amount = viplst.get(0).getPrice();
        	}
        }
        
        if (total_amount.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO)==0) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, msg);
        }
        Order order = new Order(goodsVO);
        order.setOrderNumber(OrderNum.getOrderNum());
        order.setTotalAmount(total_amount);
        //order.setTotalAmount(new BigDecimal(0.01));
        order.setCreateTime(new Date());
        order.setStatus(Constant.ORDER_UNPAY);
        order.setBuyerId(user.getId());
        order.setTradeType(Constant.TRADE_TYPE_ALIPAY);
        if(monthcout!="" && monthcout!="undefined" && monthcout!="null"){
        order.setMonthcout(Integer.parseInt(monthcout));	
        }
        if(type!="" && type!="undefined" && type!="null"){
        order.setType(Integer.parseInt(type));	
        }else{
        order.setType(3);	
        }
        //构造签名后的订单信息
        Map<String, String> params = alipayStructureParams(user, goodsVO,order);
        String signedOrder = null;
        try {
            signedOrder = AppPayCore.createLinkString(params, AppPayConfig.ALIPAY_APP_CHASET);
            String sign = AlipaySignature.rsaSign(params, AppPayConfig.ALIPAY_APP_PRIVATE_KEY, AppPayConfig.ALIPAY_APP_CHASET);
            signedOrder += "&sign=" + URLEncoder.encode(sign, AppPayConfig.ALIPAY_APP_CHASET);
            orderService.saveOrder(order);
            logger.info("生成订单, 订单号：" + order.getOrderNumber());
        } catch (AlipayApiException e) {
            logger.error("生成签名错误");
            e.printStackTrace();
            msg = "订单异常_支付宝签名异常";
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, msg);
        } catch (UnsupportedEncodingException e) {
            logger.error("生成签名错误");
            e.printStackTrace();
            msg = "订单异常_URLencode异常";
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, msg);
        } catch (Exception e) {
            logger.error("生成签名错误");
            e.printStackTrace();
            msg = "订单异常_未知异常";
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, msg);
        }

        return Result.BuildSuccessResult(signedOrder);
    }


    @RequestMapping(value = "/post_payment_notify", method = RequestMethod.POST, produces = "application/text;charset=UTF-8")
    @ResponseBody
    public String alipayNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> paramsMap = request.getParameterMap();
        Map<String, String> params = convertParamsMap(paramsMap);
        params.remove("sign_type");
        String trade_status = params.get("trade_status");//交易状态
        String trade_no = params.get("trade_no"); //支付宝流水号
        String out_trade_no = params.get("out_trade_no") ;//订单号
        String buyer_account = params.get("buyer_id");//买家第三方唯一用户号
        String total_amount = params.get("total_amount");//订单金额
        try {
            boolean result = AlipaySignature.rsaCheckV2(params, AppPayConfig.ALIPAY_APP_ALI_PUBLIC_KEY, AppPayConfig.ALIPAY_APP_CHASET);
            if (result) {
                if (trade_status.equals("TRADE_SUCCESS")) {
                    Order order = new Order();
                    order.setOrderNumber(out_trade_no);
                    List<Order> orders = orderService.queryOrders(order);
                    if (orders != null && orders.size() > 0) {
                        Order order_ = orders.get(0);
                        if (order_.getStatus().byteValue() != Constant.ORDER_FINISHED) {
                            order_.setTradeNo(trade_no);
                            Date temp = new Date();
                            Integer typey = order_.getType();//得到付款类型1.VIP解锁2.图集下载3.图集解锁
                            Integer monthcounts = order_.getMonthcout();//会员月数
                            
                           /* if(monthcounts!=null){
                            	
                            }else{
                            	monthcounts=1;
                            }*/
                            if(monthcounts!=null){
                            if(typey==1){
                            	//更新会员
                            	User Usery = new User();
                            	Usery.setId(order_.getBuyerId());;
                                User userOne =	userService.selectUser(Usery);
                             if(userOne!=null){
                            	Integer usrType = userOne.getType();//用户是否vip 1.是vip
                            	//更新vip结束时间
                            	if(usrType!=null){
                            	if(usrType==1){	
                            	//用户已经是vip	
                            	Date edtims = userOne.getEndTime();
                            	
                            	String enddates = DateUtils.GetSysDate("yyyy-MM-dd HH:mm:ss",  DateUtils.format(edtims,"yyyy-MM-dd HH:mm:ss"),   0,   monthcounts,   0);
                            	Usery.setEndTime(DateUtils.parse(enddates,"yyyy-MM-dd HH:mm:ss"));
                            	userService.updateUserVip(Usery);
                            	}else{
                                //用户不是vip
                                Usery.setType(1);
                            	Usery.setBeginTime(new Date());
                            	String enddates = DateUtils.GetSysDate("yyyy-MM-dd HH:mm:ss",  DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),   0,   monthcounts,   0);
                                Usery.setEndTime(DateUtils.parse(enddates,"yyyy-MM-dd HH:mm:ss"));
                                userService.updateUserVip(Usery);
                            	}
                            	}else{
                            		  //用户不是vip
                                    Usery.setType(1);
                                	Usery.setBeginTime(new Date());
                                	String enddates = DateUtils.GetSysDate("yyyy-MM-dd HH:mm:ss",  DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),   0,   monthcounts,   0);
                                    Usery.setEndTime(DateUtils.parse(enddates,"yyyy-MM-dd HH:mm:ss"));
                                    userService.updateUserVip(Usery);	
                            	}
                             }
                            	 
                            }
                           }
                            order_.setPaymentTime(temp);
                            order_.setFinishTime(temp);//暂时订单的结束时间与支付时间相同
                            order_.setNotifyTime(temp);
                            order_.setStatus(Constant.ORDER_FINISHED);
                            order_.setBuyerAccount(buyer_account);
                            //order_.setReceiptAmount(total_amount1); //由于支付宝接口中暂时还不能返回“实收金额”
                            //order_.setBuyerPayAmount(total_amount1);//由于支付宝接口中暂时还不能返回“付款金额”
                            orderService.updateOrderAndSaveRecord(order_);
                        }
                        logger.info("===============支付异步通知===========");
                        for (String key : params.keySet()) {
                            logger.info(key+" : "+params.get(key));
                        }
                        logger.info("支付宝异步接受通知成功，订单号：" + out_trade_no + "交易状态：" + trade_status + "支付宝流水号：" + trade_no);
                        return "success";
                    } else {
                        logger.error("支付宝异步接受通知异常，没有查询到相关订单，订单号：" + out_trade_no + "交易状态：" + trade_status + "支付宝流水号：" + trade_no);
                        return "fail";
                    }

                }else if (trade_status.equals("TRADE_CLOSED")) {
                    String refund_fee = params.get("refund_fee");
                    //交易关闭，更新订单状态
                    Order order = new Order();
                    order.setOrderNumber(out_trade_no);
                    List<Order> orders = orderService.queryOrders(order);
                    if (orders != null && orders.size() > 0) {
                        Order order_ = orders.get(0);
                        Order param = new Order();
                        param.setId(order_.getId());
                        param.setStatus(Constant.ORDER_REFUND_SUCCESS);
                        param.setRefundFee(new BigDecimal(refund_fee));
                        param.setRefundTime(new Date());
                        //根据id更新订单状态
                        orderService.updateByPrimaryKeySelective(param);
                    }
                    logger.info("===============退款异步通知===========");
                    for (String key : params.keySet()) {
                        logger.info(key+" : "+params.get(key));
                    }
                    logger.info("支付宝接受异步通知，退款成功，订单号："+out_trade_no);
                    return "success";
                }
            } else {
                logger.error("支付宝异步接受通知异常，验签失败，订单号：" + out_trade_no + "交易状态：" + trade_status + "支付宝流水号：" + trade_no);
            }
        } catch (AlipayApiException e) {
            logger.error("异步验签发生异常！");
            logger.error("支付宝异步接受通知异常，验签失败，订单号：" + out_trade_no + "交易状态：" + trade_status + "支付宝流水号：" + trade_no);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("支付宝异步接受通知异常，未知错误，订单号：" + out_trade_no + "交易状态：" + trade_status + "支付宝流水号：" + trade_no);
            e.printStackTrace();
        }
        return "fail";
    }

    private Map<String, String> convertParamsMap(Map<String, Object> paramsMap) {
        Map<String, String> params = new HashMap<String,String>();
        if (paramsMap != null) {
            Set<String> keys = paramsMap.keySet();
            for (String key : keys) {
                params.put(key, ((String[]) paramsMap.get(key))[0]);
            }
        }
        return params;
    }


    @RequestMapping(value = "/post_payment_sync_notify.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result alipaySyncNotify(String result,String resultStatus,HttpServletRequest req, HttpServletResponse resp) {
        try {
            logger.info("同步result:" + result);
            logger.info("同步resultStatus:" + resultStatus);
            if (result == null || result.equals("")) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验签失败，没有result参数");
            }
            JSONObject json = JSON.parseObject(result, Feature.OrderedField);
            String sign = json.getString("sign");
            JSONObject alipay_trade_app_pay_response = json.getJSONObject("alipay_trade_app_pay_response");
            String out_trade_no = alipay_trade_app_pay_response.getString("out_trade_no");
            String trade_no = alipay_trade_app_pay_response.getString("trade_no");
            String total_amount = alipay_trade_app_pay_response.getString("total_amount");
//            String response = "{\"code\":\"10000\",\"msg\":\"Success\",\"app_id\":\"2016093002014477\",\"auth_app_id\":\"2016093002014477\",\"charset\":\"utf-8\",\"timestamp\":\"2016-10-31 19:36:18\",\"total_amount\":\"0.10\",\"trade_no\":\"2016103121001004900282822394\",\"seller_id\":\"2088421715258605\",\"out_trade_no\":\"20161031193608746154\"}";
//            sign = "XbwQRMg/AMsf2dM7/48S7gP+YXkx1Ax8oQKPSHh0VZ4wf/J7Hwp8FplO+hO+dAH0brPgwPuvasYZ69P272gvSpZcJOd9YNeVIrFtZwJ1tn5ljtiKKAeUb4Mv5m2iN96eIXKQai2AWc/RRhpv1MUG/vC7UiI5lM0pfzaU+XcNIGY=";
            String response = alipay_trade_app_pay_response.toString();
            boolean checkResult = AlipaySignature.rsaCheckContent(response, sign, AppPayConfig.ALIPAY_APP_ALI_PUBLIC_KEY, AppPayConfig.ALIPAY_APP_CHASET);
            if (checkResult) {
                if (resultStatus.equals("9000")) {
                    Order order = new Order();
                    order.setOrderNumber(out_trade_no);
                    List<Order> orders = orderService.queryOrders(order);
                    if (orders != null && orders.size() > 0) {
                        Order order_ = orders.get(0);
                     
                        logger.info("支付宝同步接受通知成功，订单号：" + out_trade_no + "支付宝流水号：" + trade_no);
                        return Result.BuildSuccessResult("支付成功");
                    } else {
                        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "不存在该订单！");
                    }
                } else if (resultStatus.equals("6004") || resultStatus.equals("8000")) {
                    Order order = new Order();
                    order.setOrderNumber(out_trade_no);
                    List<Order> orders = orderService.queryOrders(order);
                    if (orders != null && orders.size() > 0) {
                        Order order_ = orders.get(0);
                        if (order_.getStatus().byteValue() == Constant.ORDER_FINISHED) {
                            return Result.BuildSuccessResult("支付成功");
                        } else {
                            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "支付存在异常，请查看商品是否已经购买成功！");
                        }
                    } else {
                        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "不存在该订单！");
                    }
                } else {
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "支付失败");
                }
            } else {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验签失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验签出现错误");
        }

    }

    /**
     * 支付宝根据活动id退款
     * @param activityId
     * @param req
     * @return
     */
    @RequestMapping(value = "/post_alipay_order_refund.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result alipayOrderRefund(Integer activityId,Integer modelId, HttpServletRequest req) {
        Map<Integer, String> result = new HashMap<>();
        Order params = new Order();
        params.setActivityId(activityId);
        params.setActivityModel(modelId);
        params.setStatus(Constant.ORDER_FINISHED);
        List<Order> orders = orderService.queryOrders(params);
        if (orders != null && orders.size() > 0) {
            for (Order order : orders) {
                try {
                    order.setStatus(Constant.ORDER_REFUNDING);
                    orderService.updateForStatus(order);//首先将订单状态置为退款中
                    String refund_back = orderService.alipay_refund(order);
                    result.put(order.getId(), refund_back);
                } catch (PaymentAliRefundException e) {
                    e.printStackTrace();
                    order.setStatus(Constant.ORDER_REFUND_FAIL);
                    orderService.updateForStatus(order);
                    result.put(order.getId(), e.getMessage());
                }
            }
        }
        return Result.BuildSuccessResult(result);
    }



    /**
     * 购买套图接口(wechat)
     * 该接口应为微信小程序回调接口，需要根据支付接口进行调整。
     * @return
     */
    @RequestMapping(value="/post_applet_apply_order_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result wechatOrder(GoodsVO good,String device_info, String body, String attach,
                              String time_start,int paymentType,String openId, String time_expire, String goods_tag,
                              HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
    	String passportId = req.getParameter("passportId");
    	
        User user = null;
           if(StringUtils.isNotEmpty(passportId)){
        	   logger.info("wechatOrder1:用户信息" );
               // 从缓存读个人信息
               user = userService.loadUserFromCache(passportId);
               logger.info("wechatOrder2:获取用户信息" );
           }
    	//User user = (User)req.getAttribute("user");//注释从其他的地方获取
        if(user == null){
            logger.info("wechatOrder:请先登录" );
            return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED,"未登录");
        }
         Map<String,Object> map = validateAndReturnPrice(good);
        String msg = map.get("msg").toString();
        BigDecimal total_fee = null;
        Integer _have = orderService.selectHaveBuy(user.getId());//是否首次购买
        if(_have == 0){//首次购买只需一元
            total_fee = new BigDecimal(100);
        }else{
            total_fee = (BigDecimal) map.get("price");
            //total_fee =  total_fee*new BigDecimal(100);
        	//total_fee = total_fee.multiply(new BigDecimal("100")).intValue();
        }
        if (total_fee.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO)==0) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, msg);
        }
        BigDecimal total_fee_min = total_fee.multiply(new BigDecimal(1.00)).setScale(2,BigDecimal.ROUND_HALF_UP);
        Map<String, String> params = new HashMap<>();
        //应用ID 是 String(32) 微信开放平台审核通过的应用APPID
        //微信小程序
        params.put("appid", AppPayConfig.WECHAT_XCXID);
        //商户号 是 String(32) 微信小程序支付分配的商户号
        params.put("mch_id", AppPayConfig.WECHAT_MCHIDXCX);
        params.put("openid", openId);  
        //设备号 否 String(32) 终端设备号(门店号或收银设备ID)，默认请传"WEB"
        if(device_info != null)
         params.put("device_info", device_info);
        //随机字符串 是 String(32) 随机字符串，不长于32位
        String nonce_str = AppPayCore.getNonceStr();
        params.put("nonce_str", nonce_str);

        //商品描述 是 String(128) 商品描述，如：天天爱消除-游戏充值
        if(body == null) {
            params.put("body", " 时光相册" + "-" + (good.getGoodsType() == 1 ? "图集" : "活动"));
        } else {
            params.put("body", body);
        }
        if(attach != null)
            params.put("attach", attach);
        //商户订单号 是 String(32) 商户系统内部的订单号,32个字符内、可包含字母
        String out_trade_no = OrderNum.getOrderNum();
        params.put("out_trade_no", out_trade_no);
        //货币类型 否 String(16) 符合ISO 4217标准的三位字母代码，默认人民币：CNY
        String fee_type = "CNY";
        params.put("fee_type", fee_type);
        //总金额 是 Int 订单总金额，单位为分
        params.put("total_fee", total_fee.toString());
        //终端IP 是 String(16) 用户端实际ip
        //params.put("spbill_create_ip", NetworkUtil.getIpAddress(req));
        //交易起始时间 否 String(14) 订单生成时间，格式为yyyyMMddHHmmss
        if(time_start != null)
            params.put("time_start", time_start);
        //交易结束时间 否 String(14) 订单失效时间，格式为yyyyMMddHHmmss
        if(time_expire != null)
            params.put("time_expire", time_expire);
        //商品标记 否 String(32) 商品标记，代金券或立减优惠功能的参数
        if(goods_tag != null)
            params.put("goods_tag", goods_tag);
        //通知地址 是 String(256) 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数
        params.put("notify_url", AppPayConfig.WECHATXCX_NOTIFY_URL);
        //交易类型 是 String(16) 支付类型
        params.put("trade_type", "JSAPI");//小程序支付
        String url = AppPayConfig.WECHAT_ORDER;//统一下单接口
        Map<String, String> datas = new HashMap<>();
        try {
        	 JSONArray jsonArray=new JSONArray(); 
        	// 除去数组中的空值和签名参数
        	Map sPara = PayUtil.paraFilter(params);
        	String prestr = PayUtil.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        	String key = "&key="+AppPayConfig.WECHATXCX_PREPAY_ID; // 商户支付密钥
        	//MD5运算生成签名
        	String mysign = PayUtil.sign(prestr, key, "utf-8").toUpperCase();
        	params.put("sign", mysign);
        	//打包要发送的xml
        	String respXml = XmlUtil.createXml(params);
        	// 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”
        	respXml = respXml.replace("__", "_");
        	
        	String param = respXml;
        	//String result = SendRequestForUrl.sendRequest(url, param);//发起请求
        	String results =PayUtil.httpRequest(url, "POST", param);
        	// 将解析结果存储在HashMap中
        	Map mapResult = new HashMap();
        	InputStream in=new ByteArrayInputStream(results.getBytes("utf-8")); 
        	// 读取输入流
        	SAXReader reader = new SAXReader();
        	Document document = reader.read(in);
        	// 得到xml根元素
        	Element root = document.getRootElement();
        	// 得到根元素的所有子节点
        	List<Element> elementLists = root.elements();
        	for (Element element : elementLists) {
            	mapResult.put(element.getName(), element.getText());
            }
        	// 返回信息
        	String return_codes = (String) mapResult.get("return_code");//返回状态码
        	String return_msgs = (String) mapResult.get("return_msg");//返回信息
        	
        	//JSONObject JsonObject=new JSONObject() ;
        	if(return_codes=="SUCCESS"||return_codes.equals(return_codes)){
        	// 业务结果
        	String prepay_id = (String) mapResult.get("prepay_id");//返回的预付单信息
        	String nonceStr=AppPayCore.getNonceStr();
        	datas.put("prepayIds", prepay_id+"");
            Long timeStamp= System.currentTimeMillis()/1000;
            datas.put("timeStamp", timeStamp+""); 
            datas.put("nonceStr", nonceStr); 
            String stringSignTemp = "appId="+AppPayConfig.WECHAT_XCXID+"&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp; 
        	//再次签名
        	String paySign=PayUtil.sign(stringSignTemp, "&key="+AppPayConfig.WECHATXCX_PREPAY_ID, "utf-8").toUpperCase();
        	datas.put("paySign", paySign);
        	
        	 
        	 Order order = new Order(good);
             order.setBuyerId(user.getId());
             order.setOrderNumber(out_trade_no);
             order.setTotalAmount(total_fee_min);
             order.setCreateTime(new Date());
             order.setStatus(Constant.ORDER_UNPAY);
             order.setTradeType(Constant.TRADE_TYPE_WECHAT);
             orderService.saveOrder(order);
             logger.info("微信-生成预付订单，订单ID：" + out_trade_no);
             return Result.BuildSuccessResult(datas);
        	}else{
        		logger.error("微信-生成预付订单失败，订单ID：" + out_trade_no);
                return Result.BuildFailResult(ResponseCode.SC_PRECONDITION_FAILED, "先决条件错误");
        	}
        	

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信-生成预付订单失败，订单ID：" + out_trade_no);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器异常");
        }
    }
    /**
     * 购买套图接口(wechat)
     * 该接口为微信，需要根据支付接口进行调整。
     * @return
     */
    @RequestMapping(value="/post_wechat_apply_order_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result appletOrder(GoodsVO good,String device_info, String body, String attach,
                              String time_start,String openId, String time_expire, String goods_tag,
                              HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
    	String passportId = req.getParameter("passportId");
    	
    	String type = req.getParameter("type");//得到付款类型1.VIP解锁2.图集下载3.图集解锁
    	
    	String monthcout = req.getParameter("monthcout");//会员月数
    	
        User user = null;
           if(StringUtils.isNotEmpty(passportId)){
        	   logger.info("wechatOrder1:用户信息" );
               // 从缓存读个人信息
               user = userService.loadUserFromCache(passportId);
               logger.info("wechatOrder2:获取用户信息" );
           }
    	//User user = (User)req.getAttribute("user");//注释从其他的地方获取
        if(user == null){
            logger.info("wechatOrder:请先登录" );
            return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED,"未登录");
        }
        Map<String,Object> map = validateAndReturnPrice(good);
        String msg = map.get("msg").toString();
        BigDecimal total_fee = null;
        Integer _have = orderService.selectHaveBuy(user.getId());//是否首次购买
        if(_have == 0){//首次购买只需一元
            total_fee = new BigDecimal(100);
        }else{
            total_fee = (BigDecimal)map.get("price");
            System.out.println("================"+total_fee);
            //total_fee = new BigDecimal(699);
        }
        if(monthcout!="" && monthcout!="null" && monthcout!="undefined" ){
        	Vip vip = new Vip();
        	vip.setMonthCount(Integer.parseInt(monthcout));		
        	List<Vip> viplst = vipService.selectVip(vip);
        	if(viplst!=null && viplst.size()>0){
        		total_fee = viplst.get(0).getPrice();
        	}
        }
        if (total_fee.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO)==0) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, msg);
        }
        BigDecimal total_fee_min = total_fee.multiply(new BigDecimal(1.00)).setScale(2,BigDecimal.ROUND_HALF_UP);
        Map<String, String> params = new HashMap<>();
    
        //微信支付
        params.put("appid", AppPayConfig.WECHAT_APPID);
        //商户号 是 String(32) 微信支付分配的商户号
        params.put("mch_id", AppPayConfig.WECHAT_MCHID);
      
        
        //设备号 否 String(32) 终端设备号(门店号或收银设备ID)，默认请传"WEB"
        if(device_info != null)
        params.put("device_info", device_info);
        //随机字符串 是 String(32) 随机字符串，不长于32位
        String nonce_str = AppPayCore.getNonceStr();
        params.put("nonce_str", nonce_str);

        //商品描述 是 String(128) 商品描述，如：天天爱消除-游戏充值
      /*  if(body == null) {
            params.put("body", "青豆客" + "-" + (good.getGoodsType() == 1 ? "图集" : "活动"));
        } else {
            params.put("body", body);
        }*/
        if(type=="" || type=="null" || type=="undefined"){
        params.put("body", "青豆客" + "-" +  "图集");
        }else{
        params.put("body", "青豆客" + "-" + (Integer.parseInt(type) == 1 ? "VIP会员" : "图集"));	
        }
        //附加数据 否 String(127) 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        if(attach != null)
            params.put("attach", attach);
        //商户订单号 是 String(32) 商户系统内部的订单号,32个字符内、可包含字母
        String out_trade_no = OrderNum.getOrderNum();
        params.put("out_trade_no", out_trade_no);
        //货币类型 否 String(16) 符合ISO 4217标准的三位字母代码，默认人民币：CNY
        String fee_type = "CNY";
        params.put("fee_type", fee_type);
        //总金额 是 Int 订单总金额，单位为分
        params.put("total_fee", total_fee.toString());
        //终端IP 是 String(16) 用户端实际ip
        params.put("spbill_create_ip", NetworkUtil.getIpAddress(req));
        //交易起始时间 否 String(14) 订单生成时间，格式为yyyyMMddHHmmss
        if(time_start != null)
            params.put("time_start", time_start);
        //交易结束时间 否 String(14) 订单失效时间，格式为yyyyMMddHHmmss
        if(time_expire != null)
            params.put("time_expire", time_expire);
        //商品标记 否 String(32) 商品标记，代金券或立减优惠功能的参数
        if(goods_tag != null)
            params.put("goods_tag", goods_tag);
        //通知地址 是 String(256) 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数
        params.put("notify_url", AppPayConfig.WECHAT_NOTIFY_URL);
        //交易类型 是 String(16) 支付类型
        params.put("trade_type", "APP");//小程序支付
        // params.put("trade_type", "APP");//app支付
        //指定支付方式 否 String(32) no_credit--指定不能使用信用卡支付
        params.put("limit_pay", "no_credit");
        String url = AppPayConfig.WECHAT_ORDER;
        String result = "";

        try {
            result = AppPaySubmit.buildRequest(params, url, true);
            logger.error("返回信息result：" + result);
            String returnCode = XmlUtil.parseXml(result,AppPayConfig.WECHAT_RETURN_CODE);
            logger.error("返回信息returnCode：" + returnCode);
            Map<String, String> data = null;
            if(null == result || "".equals(result)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "未返回支付消息");
            }else if(returnCode.contains(AppPayConfig.WECHAT_FAIL)){
                logger.error("微信-生成预付订单失败，订单ID：" + out_trade_no);
                logger.error("错误信息：" + result);
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"支付订单生成失败");
            }else{
            	//BigDecimal total_fee_min = total_fee.multiply(new BigDecimal(0.01)).setScale(2,BigDecimal.ROUND_HALF_UP);
                Order order = new Order(good);
                order.setBuyerId(user.getId());
                order.setOrderNumber(out_trade_no);
                order.setTotalAmount(total_fee_min);
                order.setCreateTime(new Date());
                order.setStatus(Constant.ORDER_UNPAY);
                order.setTradeType(Constant.TRADE_TYPE_WECHAT);
                if(monthcout!="" && monthcout!="undefined" && monthcout!="null"){
                order.setMonthcout(Integer.parseInt(monthcout));	
                }
                if(type!="" && type!="undefined" && type!="null"){
                order.setType(Integer.parseInt(type));	
                }else{
                order.setType(3);	
                }
                orderService.saveOrder(order);
                logger.info("微信-生成预付订单，订单ID：" + out_trade_no);
                data = resignParam(result);
                //读取返回的XML
                return Result.BuildSuccessResult(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信-生成预付订单失败，订单ID：" + out_trade_no);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器异常");
        }
    }

    //再次签名
    private Map<String, String> resignParam(String result){
        Map data = XmlUtil.parseXml(result);
        SortedMap<String, String> par = new TreeMap<String, String>();
        par.put("appid", data.get("appid").toString());
        par.put("partnerid", data.get("mch_id").toString());
        par.put("prepayid", data.get("prepay_id").toString());
        par.put("package", "Sign=WXPay");
        par.put("noncestr", AppPayCore.getNonceStr());
        //时间戳
        Date date = new Date();
        long time = date.getTime();
        //mysq 时间戳只有10位 要做处理
        String dateline = time + "";
        dateline = dateline.substring(0, 10);

        par.put("timestamp", dateline);

        String signNew = AppPayCore.getSignFromParams(par);

        logger.info("再次签名："+signNew);
        par.put("sign",signNew);
        return  par;
    }
    
    
    

    /**
     * wechat
     * 支付结果通知地址(微信服务端调用)
     * 注：同样的通知可能会多次发送
     * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
     * @return
     */
    @RequestMapping(value = "/wechat_order_notify.do")
    public String wechatOrderNotify(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String data = AppPayCore.getDataFromRequest(req);
            logger.info("微信-回调通知数据:" + data);
            String return_code = XmlUtil.parseXml(data, AppPayConfig.WECHAT_RETURN_CODE);
            if(StringUtils.isEmpty(data) || StringUtils.isEmpty(return_code)){
                logger.info("微信-接收异步通知失败");
                return "fail";
            }
            if(return_code.contains(AppPayConfig.WECHAT_FAIL)){//通信失败
                logger.info("微信-接收异步通知失败");
                return "fail";
            }else{//通信成功
                //验证签名
                Map<String, String> param = XmlUtil.parseXml(data);
                String sign = XmlUtil.parseXml(data, "sign");
                String mysign = AppPayCore.getSignFromParams(param);
                if(StringUtils.isEmpty(sign) || StringUtils.isEmpty(mysign) || !AppPayCore.verify(param,sign)){
                    logger.info("微信返回签名："+sign);
                    logger.info("生成签名："+mysign);
                    logger.info("微信-接收异步通知失败，签名错误");
                    return "fail";
                }
                String resultCode = XmlUtil.parseXml(data,AppPayConfig.WECHAT_RESULT_CODE);
                Order order = new Order();
                String transaction_id = XmlUtil.parseXml(data, "transaction_id");
                String out_trade_no = XmlUtil.parseXml(data, "out_trade_no");
                //使用商户订单号查询
                order.setOrderNumber(out_trade_no);
                List<Order> orders = orderService.queryOrders(order);
                if(orders != null && orders.size() > 0){
                    order = orders.get(0);
                    order.setTradeNo(transaction_id);
                }else {
                    logger.info("微信-接受异步通知成功，不存在此订单ID：" + order.getOrderNumber());
                    return "fail";
                }
                if(resultCode.contains(AppPayConfig.WECHAT_FAIL)){//成功接收通知(交易失败)
                    //成功接收通知(交易失败)
                    if(order.getStatus() != Constant.ORDER_FAILED){//第一次处理通知
                        order.setStatus(Constant.ORDER_FAILED);
                        order.setNotifyTime(new Date());
                        orderService.updateByPrimaryKeySelective(order);
                        logger.info("微信-接受异步通知成功，交易失败并更新订单状态，订单ID："+order.getOrderNumber());
                    }else{//非第一次处理通知
                        logger.info("微信-接受异步通知成功，交易失败，订单ID：" + order.getOrderNumber());
                    }
                    return "success";
                }else if(resultCode.contains(AppPayConfig.WECHAT_SUCCESS)){//成功接收通知(交易成功)

                    if(order.getStatus() != Constant.ORDER_FINISHED){//第一次处理通知
                        order.setStatus(Constant.ORDER_FINISHED);
                        Date temp = new Date();
                        Integer typey = order.getType();//得到付款类型1.VIP解锁2.图集下载3.图集解锁
                        Integer monthcounts = order.getMonthcout();//会员月数
                        
                      /*  if(monthcounts!=null){
                        	
                        }else{
                        	monthcounts=1;
                        }*/
                        if(monthcounts!=null){
                        if(typey==1){
                        	//更新会员
                        	User Usery = new User();
                        	Usery.setId(order.getBuyerId());;
                            User userOne =	userService.selectUser(Usery);
                         if(userOne!=null){
                        	Integer usrType = userOne.getType();//用户是否vip 1.是vip
                        	//更新vip结束时间
                        	if(usrType!=null){
                        	if(usrType==1){	
                        	//用户已经是vip	
                        	Date edtims = userOne.getEndTime();
                        	
                        	String enddates = DateUtils.GetSysDate("yyyy-MM-dd HH:mm:ss",  DateUtils.format(edtims,"yyyy-MM-dd HH:mm:ss"),   0,   monthcounts,   0);
                        	Usery.setEndTime(DateUtils.parse(enddates,"yyyy-MM-dd HH:mm:ss"));
                        	userService.updateUserVip(Usery);
                        	}else{
                            //用户不是vip
                            Usery.setType(1);
                        	Usery.setBeginTime(new Date());
                        	String enddates = DateUtils.GetSysDate("yyyy-MM-dd HH:mm:ss",  DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),   0,   monthcounts,   0);
                            Usery.setEndTime(DateUtils.parse(enddates,"yyyy-MM-dd HH:mm:ss"));
                            userService.updateUserVip(Usery);
                        	}
                        	}else{
                        		  //用户不是vip
                                Usery.setType(1);
                            	Usery.setBeginTime(new Date());
                            	String enddates = DateUtils.GetSysDate("yyyy-MM-dd HH:mm:ss",  DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),   0,   monthcounts,   0);
                                Usery.setEndTime(DateUtils.parse(enddates,"yyyy-MM-dd HH:mm:ss"));
                                userService.updateUserVip(Usery);	
                        	}
                         }
                        	 
                        }
                       }
                        order.setNotifyTime(temp);
                        order.setFinishTime(temp);
                        order.setPaymentTime(temp);
                        orderService.updateOrderAndSaveRecord(order);
                        logger.info("微信-接受异步通知成功，交易成功并更新订单状态，订单ID："+order.getOrderNumber());
                    }else{//非第一次处理通知
                        logger.info("微信-接受异步通知成功，交易成功，订单ID：" + order.getOrderNumber());
                    }
                    return "success";
                }else{
                    logger.info("微信-接收异步通知成功，但结果未知");
                    return "fail";
                }
            }

        } catch (Exception e) {
            logger.info("微信-接受异步通知失败");
            e.printStackTrace();
            return "fail";
        }
    }
    /**
     * wechat小程序
     * 支付结果通知地址(微信服务端调用)
     * 注：同样的通知可能会多次发送
     * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
     * @return
     */
    @RequestMapping(value = "/wechatApplet_order_notify.do")
    public String wechatAppletOrderNotify(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String data = AppPayCore.getDataFromRequest(req);
            logger.info("微信-回调通知数据:" + data);
            String return_code = XmlUtil.parseXml(data, AppPayConfig.WECHAT_RETURN_CODE);
            if(StringUtils.isEmpty(data) || StringUtils.isEmpty(return_code)){
                logger.info("微信-接收异步通知失败");
                return "fail";
            }
            if(return_code.contains(AppPayConfig.WECHAT_FAIL)){//通信失败
                logger.info("微信-接收异步通知失败");
                return "fail";
            }else{//通信成功
                //验证签名
                /*Map<String, String> param = XmlUtil.parseXml(data);
                Map sPara = PayUtil.paraFilter(param);
            	String prestr = PayUtil.createLinkString(sPara); 
                String sign=PayUtil.sign(prestr, "&key="+AppPayConfig.WECHATXCX_PREPAY_ID, "utf-8").toUpperCase();
                String mysign = AppPayCore.getSignFromParams(param);
                if(StringUtils.isEmpty(sign) || StringUtils.isEmpty(mysign) || !AppPayCore.verify(prestr, sign, AppPayConfig.WECHATXCX_PREPAY_ID, AppPayConfig.WECHAT_INPUT_CHARSET)){
                    logger.info("微信返回签名："+sign);
                    logger.info("生成签名："+mysign);
                    logger.info("微信-接收异步通知失败，签名错误");
                    return "fail";
                }*/
                String resultCode = XmlUtil.parseXml(data,AppPayConfig.WECHAT_RESULT_CODE);
                Order order = new Order();
                String transaction_id = XmlUtil.parseXml(data, "transaction_id");
                String out_trade_no = XmlUtil.parseXml(data, "out_trade_no");
                logger.info("out_trade_no订单ID：" + out_trade_no);
                //使用商户订单号查询
                order.setOrderNumber(out_trade_no);
                List<Order> orders = orderService.queryOrders(order);
                if(orders != null && orders.size() > 0){
                    order = orders.get(0);
                    order.setTradeNo(transaction_id);
                }else {
                    logger.info("微信-接受异步通知成功，不存在此订单ID：" + order.getOrderNumber());
                    return "fail";
                }
                if(resultCode.contains(AppPayConfig.WECHAT_FAIL)){//成功接收通知(交易失败)
                    //成功接收通知(交易失败)
                    if(order.getStatus() != Constant.ORDER_FAILED){//第一次处理通知
                        order.setStatus(Constant.ORDER_FAILED);
                        order.setNotifyTime(new Date());
                        orderService.updateByPrimaryKeySelective(order);
                        logger.info("微信-接受异步通知成功，交易失败并更新订单状态，订单ID："+order.getOrderNumber());
                    }else{//非第一次处理通知
                        logger.info("微信-接受异步通知成功，交易失败，订单ID：" + order.getOrderNumber());
                    }
                    return "success";
                }else if(resultCode.contains(AppPayConfig.WECHAT_SUCCESS)){//成功接收通知(交易成功)

                    if(order.getStatus() != Constant.ORDER_FINISHED){//第一次处理通知
                    	PhotoAlbum photoAlbums  =photoAlbumService.findById(order.getAlbumId());
                    	//修改可提现金额
                    	userService.updateCacheCount(order.getTotalAmount(), photoAlbums.getModelId());
                        order.setStatus(Constant.ORDER_FINISHED);
                        Date temp = new Date();
                        order.setNotifyTime(temp);
                        order.setFinishTime(temp);
                        order.setPaymentTime(temp);
                        orderService.updateOrderAndSaveRecord(order);
                        logger.info("微信-接受异步通知成功，交易成功并更新订单状态，订单ID："+order.getOrderNumber());
                    }else{//非第一次处理通知
                        logger.info("微信-接受异步通知成功，交易成功，订单ID：" + order.getOrderNumber());
                    }
                    return "success";
                }else{
                    logger.info("微信-接收异步通知成功，但结果未知");
                    return "fail";
                }
            }

        } catch (Exception e) {
            logger.info("微信-接受异步通知失败");
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 查询订单
     * @return
     */
    @RequestMapping(value = "/post_wechat_orderquery_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result wechatOrderQuery(String transaction_id, String out_trade_no, HttpServletRequest req){
        User user = (User)req.getAttribute("user");
        if(user == null){
            logger.info("wechatOrderQuery:请先登录");
            return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED, "未登录");
        }
        if((transaction_id == null || "".equals(transaction_id)) && (out_trade_no == null || "".equals(out_trade_no))){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "订单号、交易号不能都为空");
        }
        Map<String, String> params = new HashMap<>();
        //应用ID 是 String(32) 微信开放平台审核通过的应用APPID
        params.put("appid", AppPayConfig.WECHAT_APPID);
        //商户号 是 String(32) 微信支付分配的商户号
        params.put("mch_id", AppPayConfig.WECHAT_MCHID);
        if(!StringUtils.isEmpty(transaction_id)){
            //微信订单号 String(32) 微信的订单号，优先使用
            params.put("transaction_id", transaction_id);
        }else{
            //商户订单号 String(32) 商户系统内部的订单号，当没提供transaction_id时需要传这个
            params.put("out_trade_no", out_trade_no);
        }
        //随机字符串 是 String(32) 随机字符串，不长于32位
        String nonce_str = AppPayCore.getNonceStr();
        params.put("nonce_str", nonce_str);
        String url = AppPayConfig.WECHAT_ORDER_QUERY;

        String result = "";

        try {
            result = AppPaySubmit.buildRequest("", "", params, url);
            logger.info("微信-查询订单返回数据:" + result);
            String returnCode = XmlUtil.parseXml(result, AppPayConfig.WECHAT_RETURN_CODE);
            if(null == result || "".equals(result)){
                logger.info("微信-查询订单请求失败，订单ID：" + transaction_id != null ? transaction_id : out_trade_no);
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"请求失败");
            }else{
                //验证签名
                Map<String, String> param = XmlUtil.parseXml(result);
                String sign = XmlUtil.parseXml(result, "sign");
                String mysign = AppPayCore.getSignFromParams(param);
                if(StringUtils.isEmpty(sign) || StringUtils.isEmpty(mysign) || !AppPayCore.verify(param, sign)){
                    logger.info("微信-查询订单失败，签名错误");
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "签名错误");
                }
                if( returnCode.contains(AppPayConfig.WECHAT_FAIL)){//通信失败
                    String return_msg = XmlUtil.parseXml(result, "return_msg");
                    logger.info("微信-查询订单失败，通信失败");
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, return_msg);
                }else{//通信成功
                    String resultCode = XmlUtil.parseXml(result, AppPayConfig.WECHAT_RESULT_CODE);
                    Order order = new Order();
                    //使用商户订单号查询
                    order.setOrderNumber(out_trade_no);
                    List<Order> orders = orderService.queryOrders(order);
                    if(orders != null && orders.size() > 0){
                        order = orders.get(0);
                    }else {
                        logger.info("微信-查询订单不存在此订单，订单ID：" + transaction_id != null ? transaction_id : out_trade_no);
                        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "不存在此订单");
                    }
                    if(resultCode.contains(AppPayConfig.WECHAT_FAIL)){//交易失败
                        if(order.getStatus() != Constant.ORDER_FAILED){
                            order.setStatus(Constant.ORDER_FAILED);
                            orderService.updateForStatus(order);
                            logger.info("微信-查询订单并成功更新状态(订单状态-失败)");
                        }else{
                            logger.info("微信-查询订单成功(订单状态-失败)");
                        }
                    }else if(resultCode.contains(AppPayConfig.WECHAT_SUCCESS)){//交易成功
                        String trade_state = XmlUtil.parseXml(result, "trade_state");//交易状态
                        String trade_state_desc = XmlUtil.parseXml(result, "trade_state_desc");//订单描述
                        if(StringUtils.isNotEmpty(trade_state)){
                            WechatOrderCode woc = WechatOrderCode.getWechatOrderCode(trade_state);
                            byte status = 0;
                            if(woc != null)
                                status = woc.getStatus();
                            if(status > 0 && order.getStatus() != status){
                                order.setStatus(status);
                                Date temp = new Date();
                                order.setFinishTime(temp);
                                order.setPaymentTime(temp);
                                orderService.updateOrderAndSaveRecord(order);
                                logger.info("微信-查询订单并成功更新状态(订单状态-"+trade_state+")");
                            }else{
                                logger.info("微信-查询订单成功(订单状态-"+trade_state+")");
                            }
                        }
                    }
                    return Result.BuildSuccessResult(order);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信-查询订单失败，订单ID：" + transaction_id != null ? transaction_id : out_trade_no);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    }

    /**
     * 组装请求参数
     * @param user
     * @param goodsVO
     * @return
     */
    private Map<String, String> alipayStructureParams(User user, GoodsVO goodsVO,Order order) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("app_id", AppPayConfig.ALIPAY_APP_ID);
        params.put("method", AppPayConfig.ALIPAY_APP_PAY_METHOD);
        params.put("format", AppPayConfig.ALIPAY_APP_FORMAT);
        params.put("charset", AppPayConfig.ALIPAY_APP_CHASET);
        params.put("sign_type", AppPayConfig.ALIPAY_APP_SIGN_TYPE);
        SimpleDateFormat sdf = new SimpleDateFormat(AppPayConfig.ALIPAY_APP_TIME_FORMAT);
        params.put("timestamp",sdf.format(new Date()));
        params.put("version", AppPayConfig.ALIPAY_APP_VERSION);
        params.put("notify_url", AppPayConfig.ALIPAY_APP_NOTIFY_URL);
        String body = "青豆客";
        if (goodsVO.getGoodsType().byteValue() == Constant.GOODS_TYPE_ALBUM) {
            body += "_" + user.getId() + "_" + goodsVO.getGoodsType() + "_" + goodsVO.getAlbumId() + "_" + goodsVO.getAlbumLevel() + "_";
        } else {
            body += "_" + user.getId() + "_" + goodsVO.getGoodsType() + "_" + goodsVO.getActivityId() + "_" + goodsVO.getActivityModel() + "_" + goodsVO.getActivityRule();
        }
        AlipayBizContent alipayBizContent = new AlipayBizContent(body,order.getOrderNumber(), order.getTotalAmount().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()+"");
        params.put("biz_content",JSON.toJSONString(alipayBizContent));
        return params;
    }




    /**
     *  校验商品是否存在并返回价钱，返回0则不存在该商品
     * @param goodsVO
     * @return
     */
    private Map<String,Object> validateAndReturnPrice(GoodsVO goodsVO) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "";
        BigDecimal price = new BigDecimal(0.00);
        try {
            //购买图集
            if (goodsVO.getGoodsType()!=null&&goodsVO.getGoodsType().byteValue() == Constant.GOODS_TYPE_ALBUM) {
                if (goodsVO.getAlbumId() != null && goodsVO.getAlbumId().intValue() > 0 && goodsVO.getAlbumLevel() > 0) {
                    PhotoAlbum photoAlbum = photoAlbumService.loadPhotoAlbum(goodsVO.getAlbumId());
                    if (photoAlbum == null || photoAlbum.getStatus() != Constant.ALBUM_SALE) {
                        msg = "该图集已不再销售！";
                    } else {
                        msg = "";
                        switch (goodsVO.getAlbumLevel()) {
                            case 1:
                                price = photoAlbum.getPrice3();
                                break;
                            case 2:
                                price = photoAlbum.getDownprice();
                                break;
                            case 3:
                                price = photoAlbum.getViewprice();
                                break;
                            default:
                                msg = "购买图集等级错误！";
                                break;
                        }
                    }
                } else {
                    msg = "订单错误！";
                }
            } else if (goodsVO.getGoodsType()!=null&&goodsVO.getGoodsType().byteValue() == Constant.GOODS_TYPE_ACTIVITY) {
                if (goodsVO.getActivityId() != null && goodsVO.getActivityId() > 0 && goodsVO.getActivityRule() != null &&
                        goodsVO.getActivityRule() > 0 && goodsVO.getActivityModel() != null && goodsVO.getActivityModel() > 0) {
                    Activity activity = activityService.loadActivity(goodsVO.getActivityId());
                    ActivityRule activityRule = activityService.loadActivityRule(goodsVO.getActivityRule());
                    ActivityModel activityModel = null;
                    if (activity != null) {
                        ActivityModel modelParams = new ActivityModel();
                        modelParams.setActivityId(activity.getId());
                        modelParams.setModelId(goodsVO.getActivityModel());
                        modelParams.setPageNo(1);
                        modelParams.setPageSize(1);
                        List<ActivityModel> models = activityService.queryModels(modelParams);
                        if (models != null && models.size() > 0) {
                            activityModel = models.get(0);
                        }
                    }
                    int supportCount = activityService.countSupporterTimes(activity.getId(),goodsVO.getActivityRule(),goodsVO.getActivityModel());
                    if (activity != null && activityRule != null && activityModel != null) {
                        if (activityRule.isRandomFeeSupport()) {
                            if (goodsVO.getActivityFreeSupportMoney()== null || goodsVO.getActivityFreeSupportMoney().compareTo(BigDecimal.ZERO) == 0) {
                                msg = "无偿支持请输入支持金额";
                            } else {
                                price = goodsVO.getActivityFreeSupportMoney().setScale(2,BigDecimal.ROUND_HALF_UP);
                            }

                        } else {
                            if (activityRule.getSuporterLimit()!=0 && supportCount >= activityRule.getSuporterLimit()) {
                                msg = "该规则已超出支持限制";
                            } else {
                                price = activityRule.getMoney();
                            }
                        }
                    } else {
                        msg = "订单错误";
                    }
                } else {
                    msg = "订单错误！";
                }

            } else {
                msg = "订单错误，没有该商品！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "订单错误！";
        }
        map.put("msg", msg);
        map.put("price", price);
        return  map;
    }

    @RequestMapping(value = "/post_been_transaction.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result queryRecordQingdouS(HttpServletRequest req,int pageNo,int pageSize){
        String passportId = req.getParameter("passportId");
        if(StringUtils.isTrimEmpty(passportId)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"未登录");
        }
        User user = null;
        if(com.tmg.utils.StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            user = userService.loadUserFromCache(passportId);
        }
        List<RecordQingdou> list = null;
        try {
             list = salesRecordService.queryRecordQingdouS(user.getId(),
                    pageNo, pageSize);
        }catch (Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
        return Result.BuildSuccessResult(list);
    }
}
