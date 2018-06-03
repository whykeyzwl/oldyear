package com.wishes.yearOld.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.tmg.utils.StringUtils;
import com.wishes.yearOld.apppay.AppPayConfig;
import com.wishes.yearOld.apppay.AppPayCore;
import com.wishes.yearOld.apppay.AppPaySubmit;
import com.wishes.yearOld.apppay.OrderNum;
import com.wishes.yearOld.apppay.WapPayCore;
import com.wishes.yearOld.apppay.WapPaySubmit;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.HttpUtil;
import com.wishes.yearOld.common.NetworkUtil;
import com.wishes.yearOld.common.QRCodeUtil;
import com.wishes.yearOld.model.ActivityModel;
import com.wishes.yearOld.model.AlipayBizContent;
import com.wishes.yearOld.model.GoodsVO;
import com.wishes.yearOld.model.Order;
import com.wishes.yearOld.model.ResponseCode;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.service.ActivityService;
import com.wishes.yearOld.service.IUserService;
import com.wishes.yearOld.service.OrderService;
import com.wishes.yearOld.service.PhotoAlbumService;
import com.wishes.yearOld.utils.XmlUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tmg-yesky on 2017/1/17.
 */
@Controller
public class WapPaymentController {

    private static final Logger logger = Logger.getLogger(WapPaymentController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    PhotoAlbumService photoAlbumService;

    @Autowired
    ActivityService activityService;
    
    @Autowired
    private IUserService userService;
    String  pageTypey="2";//默认是2
    /**
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_4
     * @param good
     * @param device_info 设备号   否
     * @param body 商品描述        是
     * @param attach 附加数据      否
     * @param time_start 交易起始时间 否
     * @param time_expire 交易结束时间 否
     * @param goods_tag 商品标记 否
     * @param openId 用户唯一标识 是
     * @param req
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value="/wap/pay/post_wechat_apply_order_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result wechatOrder(GoodsVO good, String device_info, String body, String attach,String time_start, String time_expire, String goods_tag, String openId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	  String passportId = req.getParameter("passportId");
          User user = null;
             if(StringUtils.isNotEmpty(passportId)){
                 // 从缓存读个人信息
                 user = userService.loadUserFromCache(passportId);
             }else{
          	   return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED, "未登录");
             }
        Map<String,Object> map = orderService.validateAndReturnPrice(good);
        String msg = map.get("msg").toString();
        BigDecimal total_fee = (BigDecimal) map.get("price");
        if (total_fee.doubleValue() <= 0.00) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, msg);
        }
        int total_fee_min = total_fee.multiply(new BigDecimal("100")).intValue();
        Map<String, String> params = new HashMap<>();
        //应用ID 是 String(32) 微信开放平台审核通过的应用APPID
        params.put("appid", AppPayConfig.WECHAT_PUBLIC_APPID);
        //商户号 是 String(32) 微信支付分配的商户号
        params.put("mch_id", AppPayConfig.WECHAT_PUBLIC_MCHID);
        //设备号 否 String(32) 终端设备号(门店号或收银设备ID)，默认请传"WEB"
        if(device_info != null)
            params.put("device_info", device_info);
        //随机字符串 是 String(32) 随机字符串，不长于32位
        String nonce_str = AppPayCore.getNonceStr();
        params.put("nonce_str", nonce_str);
        //商品描述 是 String(128) 商品描述，如：天天爱消除-游戏充值
        if(body == null)
            params.put("body", "青豆客" + "-" + (good.getGoodsType() == 1 ? "套图":"活动"));
        else
            params.put("body", body);
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
        params.put("total_fee", Integer.toString(total_fee_min));
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
        params.put("notify_url", AppPayConfig.WECHATWAP_NOTIFY_URL);
        //交易类型 是 String(16) 支付类型(JSAPI:公众号支付)
        params.put("trade_type", "JSAPI");
        //商品ID 否 String(32) trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID
        String product_id = good.getGoodsType() == 1 ? good.getAlbumId().toString():good.getActivityId().toString();
        params.put("product_id", product_id);
        //指定支付方式 否 String(32) no_credit--指定不能使用信用卡支付
        params.put("limit_pay", "no_credit");
        //用户标识	否 String(128) trade_type=JSAPI时（即公众号支付），此参数必传
        logger.info("微信WAP-opendId:"+openId);
        if(StringUtils.isNotEmpty(openId))
            params.put("openid", openId);
        else
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "openId 为空");
        String url = AppPayConfig.WECHAT_ORDER;
        String result = "";

        try {
            result = WapPaySubmit.buildRequest(params, url, true);
            logger.info("微信WAP-统一下单接口返回数据:" + result);
            if(StringUtils.isEmpty(result)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "未返回支付消息");
            }else{
                //InputStream stream = new ByteArrayInputStream(result.getBytes(AppPayConfig.WECHAT_INPUT_CHARSET));
                String returnCode = XmlUtil.parseXml(result, AppPayConfig.WECHAT_RETURN_CODE);
                if(returnCode == null){
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "错误");
                }
                if(returnCode.contains(AppPayConfig.WECHAT_FAIL)){//通信失败
                    String return_msg = XmlUtil.parseXml(result, "return_msg");
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "下单失败");
                }else if(returnCode.contains(AppPayConfig.WECHAT_SUCCESS)){//通信成功
                    String result_code = XmlUtil.parseXml(result, AppPayConfig.WECHAT_RESULT_CODE);
                    if(result_code.contains(AppPayConfig.WECHAT_SUCCESS)){//交易成功
                        String prepay_id = XmlUtil.parseXml(result, AppPayConfig.WECHAT_PREPAY_ID);
                        if(StringUtils.isEmpty(prepay_id))
                            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "预支付交易会话标识为空");
                        Order order = new Order(good);
                        order.setBuyerId(user.getId());
                        order.setOrderNumber(out_trade_no);
                        order.setTotalAmount(total_fee);
                        order.setCreateTime(new Date());
                        order.setStatus(Constant.ORDER_UNPAY);
                        order.setTradeType(Constant.TRADE_TYPE_WECHAT);
                        orderService.saveOrder(order);
                        logger.info("微信WAP-生成预付订单，订单ID：" + out_trade_no);
                        Map<String, Object> m = new HashMap<>();
                        Map<String, String> _m = buildWXJsApiParam(prepay_id);
                        m.put("param", _m);
                        m.put("out_trade_no", out_trade_no);
                        return Result.BuildSuccessResult(m);
                    }else{//交易失败
                        String err_code = XmlUtil.parseXml(result, "err_code");//错误码
                        String err_code_des = XmlUtil.parseXml(result, "err_code_des");//错误描述
                        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, err_code_des);
                    }
                }else {
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "下单失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信WAP-生成预付订单失败，订单ID：" + out_trade_no);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器异常");
        }
    }

    /**
     * 生成微信jsapi支付参数
     * @param prepay_id
     * @return
     * eg:
     *  "appId" : "wx2421b1c4370ec43b",     //公众号名称，由商户传入
     *  "timeStamp":" 1395712654",         //时间戳，自1970年以来的秒数
     *  "nonceStr" : "e61463f8efa94090b1f366cccfbbb444", //随机串
     *  "package" : "prepay_id=u802345jgfjsdfgsdg888",
     *  "signType" : "MD5",         //微信签名方式：
     *  "paySign" : "70EA570631E4BB79628FBCA90534C63FF7FADD89" //微信签名
     */
    private static Map<String, String> buildWXJsApiParam(String prepay_id){
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
        String nonceStr = AppPayCore.getNonceStr();
        String pk = "prepay_id=" + prepay_id;
        String signType = "MD5";

        Map<String, String> map = new HashMap<>();
        map.put("appId", AppPayConfig.WECHAT_PUBLIC_APPID);
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", pk);
        map.put("signType", signType);

        String paySign = WapPayCore.getSignFromParams(map);
        map.put("paySign", paySign);
        return map;
    }
    /**
     * wechat
     * 支付结果通知地址(微信服务端调用)
     * 注：同样的通知可能会多次发送
     * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
     * @return
     */
    @RequestMapping(value = "/wap/pay/wechat_order_notify.do")
    public void wechatOrderNotify(HttpServletRequest req, HttpServletResponse resp) {
        PrintWriter writer = null;
        String result = "<xml>\n  <return_code><![CDATA[%s]]></return_code>\n  <return_msg><![CDATA[%s]]></return_msg>\n</xml>";
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/xml; charset=UTF-8");
        try {
            writer = resp.getWriter();
            String data = HttpUtil.getDataFromRequest(req);
            logger.info("微信-回调通知数据:" + data);
            String returnCode = XmlUtil.parseXml(data, AppPayConfig.WECHAT_RETURN_CODE);
            if(StringUtils.isEmpty(data) || StringUtils.isEmpty(returnCode)){
                writer.write(String.format(result, "FAIL", "接收通知失败"));
                writer.flush();
                logger.info("微信-接收异步通知失败");
                return;
            }
            if(returnCode.contains(AppPayConfig.WECHAT_FAIL)){//通信失败
                writer.write(String.format(result, "FAIL", "接收通知失败"));
                writer.flush();
                logger.info("微信-接收异步通知失败");
                return;
            }else{//通信成功
                //验签
                Map<String, String> param = XmlUtil.parseXml(data);
                String sign = XmlUtil.parseXml(data, "sign");
                String mysign = WapPayCore.getSignFromParams(param);
               /* if(StringUtils.isEmpty(sign) || StringUtils.isEmpty(mysign) || !WapPayCore.verify(param, sign)){
                    writer.write(String.format(result, "FAIL", "签名错误"));
                    writer.flush();
                    logger.info("微信-接收异步通知失败，签名错误");
                    return;
                }*/
                String resultCode = XmlUtil.parseXml(data, AppPayConfig.WECHAT_RESULT_CODE);
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
                    writer.write(String.format(result, "SUCCESS", "接收通知成功，订单不存在"));
                    writer.flush();
                    logger.info("微信-接受异步通知成功，不存在此订单ID：" + order.getOrderNumber());
                    return;
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
                    writer.write(String.format(result, "SUCCESS", "OK"));
                    writer.flush();
                    return;
                }else if(resultCode.contains(AppPayConfig.WECHAT_SUCCESS)){//成功接收通知(交易成功)

                    if(order.getStatus() != Constant.ORDER_FINISHED){//第一次处理通知
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
                    writer.write(String.format(result, "SUCCESS", "OK"));
                    writer.flush();
                    return;
                }else{
                    writer.write(String.format(result, "SUCCESS", "接收通知成功，但结果未知"));
                    logger.info("微信-接收异步通知成功，但结果未知");
                    writer.flush();
                    return;
                }
            }
        } catch (IOException e) {
            writer.write(String.format(result, "FAIL", "接收通知失败"));
            logger.error("微信-接受异步通知失败");
            writer.flush();
            e.printStackTrace();
            return;
        }finally {
            if(writer != null)
                writer.close();
        }
    }

    /**
     * wap  alipay_提交订单(包括购买图集，购买活动规则)_返回签名后的订单信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/wap/pay/post_alipay_apply_order_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public   Result alipayApplyOrder(HttpServletRequest request,GoodsVO goodsVO) {
        //获取当前用户
      /*  User user = (User)request.getSession().getAttribute("user");
        //校验商品是否存在并返回价钱，返回0则不存在该商品
        if(user==null){
            return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED, "未登录");
        }*/
        String passportId = request.getParameter("passportId");
        User user = null;
           if(StringUtils.isNotEmpty(passportId)){
               // 从缓存读个人信息
               user = userService.loadUserFromCache(passportId);
           }else{
        	   return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED, "未登录");
           }
        Map<String,Object> map = orderService.validateAndReturnPrice(goodsVO);
        String msg = map.get("msg").toString();
        BigDecimal total_amount = (BigDecimal) map.get("price");
        if (total_amount.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO)==0) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, msg);
        }
        Order order = new Order(goodsVO);
        order.setOrderNumber(OrderNum.getOrderNum());
        order.setTotalAmount(total_amount);
        order.setCreateTime(new Date());
        order.setStatus(Constant.ORDER_UNPAY);
        order.setBuyerId(user.getId());
        order.setTradeType(Constant.TRADE_TYPE_ALIPAY);
        //构造签名后的订单信息
        Map<String, String> params = alipayStructureParams(user, goodsVO,order);
        pageTypey =goodsVO.getPageType().toString();//赋值存放页面类型
        String signedOrder = null;
        try {
            signedOrder = AppPayCore.createLinkString(params, AppPayConfig.ALIPAY_APP_CHASET);
            String sign = AlipaySignature.rsaSign(params, AppPayConfig.ALIPAY_WAP_PRIVATE_KEY, AppPayConfig.ALIPAY_APP_CHASET);
            signedOrder =AppPayConfig.ALIPAY_APP_ALIPAY_GATEWAY+"?"+signedOrder+ "&sign=" + URLEncoder.encode(sign, AppPayConfig.ALIPAY_APP_CHASET);
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

    /**
     * 支付宝同步通知（支付完成之后，支付宝页面重定向到该页面）
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/wap/pay/alipay_payment_sync_notify")
    @ResponseBody
    public ModelAndView alipaySyncNotify(HttpServletRequest req, HttpServletResponse resp) {
        String goodType = null;
        int albumOrActivityId = 0;
        int activityModelId = 0;
        Map<String,Object> paramsMap = req.getParameterMap();
        Map<String, String> params = convertParamsMap(paramsMap);
        logger.info("===============WAP支付宝支付同步通知===========");
        for (String key : params.keySet()) {
            logger.info(key+" : "+params.get(key));
        }
        String out_trade_no = params.get("out_trade_no");
        String trade_no = params.get("trade_no");
        logger.info("===============WAP支付宝支付同步通知pageTypey==========="+pageTypey);
        
        String pageTypes = pageTypey;
        if(pageTypes==null || pageTypes=="" || pageTypes=="null"){
        	pageTypes="2";
        }
        //String sign = params.get("sign");
        //String sign_type = params.get("sign_type");
        params.remove("sign_type");
        //boolean checkResult = AlipaySignature.rsaCheckV2(params, AppPayConfig.ALIPAY_WAP_ALI_PUBLIC_KEY, AppPayConfig.ALIPAY_APP_CHASET);
       String url = "http://m.qingdouke.com";
        Order order = new Order();
        order.setOrderNumber(out_trade_no);
        List<Order> orders = orderService.queryOrders(order);
        if (orders != null && orders.size() > 0) {
            Order _order = orders.get(0);
            goodType = _order.getOrderType() + "";
            if (goodType.equals("1")) {
                albumOrActivityId = _order.getAlbumId();
                if(pageTypes.equals(AppPayConfig.PAGE_XQ)){
                	url ="http://m.qingdouke.com/view/qdk-xiangqing.html?picId="+albumOrActivityId;
                }else{
                	url ="http://m.qingdouke.com/view/qdk-pic.html?picId="+albumOrActivityId;
                }
                 
                //url = "http://m.qingdouke.com/pic/wap_" + albumOrActivityId + ".html";
            } else {
                albumOrActivityId = _order.getActivityId();
                activityModelId = _order.getActivityModel();
                ActivityModel model = new ActivityModel();
                model.setActivityId(albumOrActivityId);
                model.setModelId(activityModelId);
                List<ActivityModel> list = activityService.queryModels(model);
                url = "http://m.qingdouke.com/travel/model/wap_" + list.get(0).getActivityId()+"_"+list.get(0).getModelId()+".html";
            }
        }

        return new ModelAndView("redirect:"+ url);
    }
    
    /**
     * @param req
     * @param resp
     * @param out_trade_no 商户订单ID
     * @return
     */
    @RequestMapping(value="/wap/pay/checkOrderStatus.json")
    @ResponseBody
    public Result checkOrderStatus(HttpServletRequest req, HttpServletResponse resp, @RequestParam("out_trade_no")String out_trade_no){
        User user = (User)req.getSession().getAttribute("user");
        if(user == null)
            return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED, "未登录");
        Order order = new Order();
        order.setOrderNumber(out_trade_no);
        List<Order> orders = orderService.queryOrders(order);
        if(orders != null && orders.size() > 0){
            order = orders.get(0);
            if(order.getStatus() == Constant.ORDER_FINISHED) {
                logger.info("微信-查询订单状态-交易成功:"+out_trade_no);
                return Result.BuildSuccessResult(order);
            }else {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "订单未支付完成");
            }
        }else{
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "订单不存在");
        }

    }

    /**
     * 支付宝异步通知
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/wap/pay/alipay_payment_notify", method = RequestMethod.POST, produces = "application/text;charset=UTF-8")
    @ResponseBody
    public String alipayNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> paramsMap = request.getParameterMap();
        Map<String, String> params = convertParamsMap(paramsMap);
        logger.info("===============wap支付异步通知===========");
        for (String key : params.keySet()) {
            logger.info(key+" : "+params.get(key));
        }
        params.remove("sign_type");
        String trade_status = params.get("trade_status");//交易状态
        String trade_no = params.get("trade_no"); //支付宝流水号
        String out_trade_no = params.get("out_trade_no") ;//订单号
        String buyer_account = params.get("buyer_id");//买家第三方唯一用户号
        //String total_amount = params.get("total_amount");//订单金额
        try {
            boolean result = AlipaySignature.rsaCheckV2(params, AppPayConfig.ALIPAY_WAP_ALI_PUBLIC_KEY, AppPayConfig.ALIPAY_APP_CHASET);
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
                            order_.setPaymentTime(temp);
                            order_.setFinishTime(temp);//暂时订单的结束时间与支付时间相同
                            order_.setNotifyTime(temp);
                            order_.setStatus(Constant.ORDER_FINISHED);
                            order_.setBuyerAccount(buyer_account);
                            //order_.setReceiptAmount(total_amount1); //由于支付宝接口中暂时还不能返回“实收金额”
                            //order_.setBuyerPayAmount(total_amount1);//由于支付宝接口中暂时还不能返回“付款金额”
                            orderService.updateOrderAndSaveRecord(order_);
                        }
                        logger.info("支付宝异步接受通知成功，订单号：" + out_trade_no + "交易状态：" + trade_status + "支付宝流水号：" + trade_no);
                        return "success";
                    } else {
                        logger.error("支付宝异步接受通知异常，没有查询到相关订单，订单号：" + out_trade_no + "交易状态：" + trade_status + "支付宝流水号：" + trade_no);
                        return "fail";
                    }

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

    /**
     * 组装请求参数
     * @param user
     * @param goodsVO
     * @return
     */
    private Map<String, String> alipayStructureParams(User user, GoodsVO goodsVO,Order order) {
        Map<String,String> params = new HashMap();
        params.put("app_id", AppPayConfig.ALIPAY_WAP_APPID);
        params.put("method", AppPayConfig.ALIPAY_WAP_PAY_SERVICE);
        params.put("format", AppPayConfig.ALIPAY_APP_FORMAT);
        params.put("charset", AppPayConfig.ALIPAY_APP_CHASET);
        params.put("sign_type", AppPayConfig.ALIPAY_APP_SIGN_TYPE);
        SimpleDateFormat sdf = new SimpleDateFormat(AppPayConfig.ALIPAY_APP_TIME_FORMAT);
        params.put("timestamp",sdf.format(new Date()));
        params.put("version", AppPayConfig.ALIPAY_APP_VERSION);
        params.put("notify_url", AppPayConfig.ALIPAY_WAP_NOTIFY_URL);
        String body = "青豆客";
        if (goodsVO.getGoodsType().byteValue() == Constant.GOODS_TYPE_ALBUM) {
          params.put("return_url","http://m.qingdouke.com/wap/pay/alipay_payment_sync_notify");
            body += "_" + user.getId() + "_" + goodsVO.getGoodsType() + "_" + goodsVO.getAlbumId() + "_" + goodsVO.getAlbumLevel() ;
        } else {
          params.put("return_url","http://m.qingdouke.com/wap/pay/alipay_payment_sync_notify");
            body += "_" + user.getId() + "_" + goodsVO.getGoodsType() + "_" + goodsVO.getActivityId() + "_" + goodsVO.getActivityModel() + "_" + goodsVO.getActivityRule();
        }
        AlipayBizContent alipayBizContent =
                new AlipayBizContent(body,order.getOrderNumber(), order.getTotalAmount().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()+"");
        params.put("biz_content", JSON.toJSONString(alipayBizContent));
        return params;
    }
    
    /**
     * 扫码支付
     * @param good
     * @param device_info 设备号   否
     * @param body 商品描述        是
     * @param attach 附加数据      否
     * @param time_start 交易起始时间 否
     * @param time_expire 交易结束时间 否
     * @param goods_tag 商品标记 否
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/wap/scanCode/post_wechat_apply_order_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result wechatOrderScanCode(GoodsVO good, String device_info, String body, String attach,
                              String time_start, String time_expire, String goods_tag,
                              HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	 String passportId = req.getParameter("passportId");
         User user = null;
            if(StringUtils.isNotEmpty(passportId)){
                // 从缓存读个人信息
                user = userService.loadUserFromCache(passportId);
            }else{
         	   return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED, "未登录");
            }
        Map<String,Object> map = orderService.validateAndReturnPrice(good);
        String msg = map.get("msg").toString();
        BigDecimal total_fee = (BigDecimal) map.get("price");
        if (total_fee.doubleValue() <= 0.00) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, msg);
        }
        int total_fee_min = total_fee.multiply(new BigDecimal("100")).intValue();
        Map<String, String> params = new HashMap<>();
        //应用ID 是 String(32) 微信开放平台审核通过的应用APPID
        params.put("appid", AppPayConfig.WECHAT_PUBLIC_APPID);
        //商户号 是 String(32) 微信支付分配的商户号
        params.put("mch_id", AppPayConfig.WECHAT_PUBLIC_MCHID);
        //设备号 否 String(32) 终端设备号(门店号或收银设备ID)，默认请传"WEB"
        if(device_info != null)
            params.put("device_info", device_info);
        //随机字符串 是 String(32) 随机字符串，不长于32位
        String nonce_str = AppPayCore.getNonceStr();
        params.put("nonce_str", nonce_str);
        //商品描述 是 String(128) 商品描述，如：天天爱消除-游戏充值
        if(body == null)
            params.put("body", "青豆客" + "-" + (good.getGoodsType() == 1 ? "套图":"活动"));
        else
            params.put("body", body);
        //商品详情 否 String(8192) 商品详细列表，使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来。
        //商品价格、数量问题(一个还是多个)?
        /*GoodsDetail goodsDetail = new GoodsDetail(String.valueOf(good.getGoodsType() == 1 ? good.getAlbumId():good.getActivityId()), good.getGoodName(), good.getQuantity(), total_fee_min);
        List<GoodsDetail> goods = Arrays.asList(goodsDetail);
        final List<GoodsDetail> goods_ = goods;
        Map<String, Object> goodsJson = new HashMap<String, Object>(){{put("goods_detail", goods_);}};
        params.put("detail", JSON.toJSONString(goodsJson));*/
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
        params.put("total_fee", Integer.toString(total_fee_min));
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
        params.put("notify_url", AppPayConfig.WECHATSCANCODE_NOTIFY_URL);
        //交易类型 是 String(16) 支付类型(NATIVE:扫码支付)
        params.put("trade_type", "NATIVE");
        //商品ID 否 String(32) trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID
        String product_id = good.getGoodsType() == 1 ? good.getAlbumId().toString():good.getActivityId().toString();
        params.put("product_id", product_id);
        //指定支付方式 否 String(32) no_credit--指定不能使用信用卡支付
        params.put("limit_pay", "no_credit");

        String url = AppPayConfig.WECHAT_ORDER;
        String result = "";

        try {
            result = WapPaySubmit.buildRequest(params, url, true);
            logger.info("微信-统一下单接口返回数据:" + result);
            if(StringUtils.isEmpty(result)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "未返回支付消息");
            }else{
                //InputStream stream = new ByteArrayInputStream(result.getBytes(AppPayConfig.WECHAT_INPUT_CHARSET));
                String returnCode = XmlUtil.parseXml(result, AppPayConfig.WECHAT_RETURN_CODE);
                if(returnCode == null){
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "错误");
                }
                if(returnCode.contains(AppPayConfig.WECHAT_FAIL)){//通信失败
                    String return_msg = XmlUtil.parseXml(result, "return_msg");
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "下单失败");
                }else if(returnCode.contains(AppPayConfig.WECHAT_SUCCESS)){//通信成功
                    String result_code = XmlUtil.parseXml(result, AppPayConfig.WECHAT_RESULT_CODE);
                    if(result_code.contains(AppPayConfig.WECHAT_SUCCESS)){//交易成功
                        String code_url = XmlUtil.parseXml(result, AppPayConfig.WECHAT_CODE_URL);
                        code_url = URLEncoder.encode(code_url, AppPayConfig.WECHAT_INPUT_CHARSET);
                        Order order = new Order(good);
                        order.setBuyerId(user.getId());
                        order.setOrderNumber(out_trade_no);
                        order.setTotalAmount(total_fee);
                        order.setCreateTime(new Date());
                        order.setStatus(Constant.ORDER_UNPAY);
                        order.setTradeType(Constant.TRADE_TYPE_WECHAT);
                        orderService.saveOrder(order);
                        logger.info("微信-生成预付订单，订单ID：" + out_trade_no);
                        Map<String, String> _m = new HashMap<>();
                        _m.put("code_url", code_url);
                        _m.put("out_trade_no", out_trade_no);
                        return Result.BuildSuccessResult(_m);
                    }else{//交易失败
                        String err_code = XmlUtil.parseXml(result, "err_code");//错误码
                        String err_code_des = XmlUtil.parseXml(result, "err_code_des");//错误描述
                        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, err_code_des);
                    }
                }else {
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "下单失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信-生成预付订单失败，订单ID：" + out_trade_no);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器异常");
        }
    }
    
    
    /**
     * 扫码支付
     * 支付结果通知地址(微信服务端调用)
     * 注：同样的通知可能会多次发送
     * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
     * @return
     */
    @RequestMapping(value = "/wap/scanCode/wechat_order_notify.do")
    public void wechatOrderNotifyScanCode(HttpServletRequest req, HttpServletResponse resp) {
        PrintWriter writer = null;
        String result = "<xml>\n  <return_code><![CDATA[%s]]></return_code>\n  <return_msg><![CDATA[%s]]></return_msg>\n</xml>";
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/xml; charset=UTF-8");
        try {
            writer = resp.getWriter();
            String data = HttpUtil.getDataFromRequest(req);
            logger.info("微信-回调通知数据:" + data);
            String returnCode = XmlUtil.parseXml(data, AppPayConfig.WECHAT_RETURN_CODE);
            if(StringUtils.isEmpty(data) || StringUtils.isEmpty(returnCode)){
                writer.write(String.format(result, "FAIL", "接收通知失败"));
                writer.flush();
                logger.info("微信-接收异步通知失败");
                return;
            }
            if(returnCode.contains(AppPayConfig.WECHAT_FAIL)){//通信失败
                writer.write(String.format(result, "FAIL", "接收通知失败"));
                writer.flush();
                logger.info("微信-接收异步通知失败");
                return;
            }else{//通信成功
                //验签
                Map<String, String> param = XmlUtil.parseXml(data);
                String sign = XmlUtil.parseXml(data, "sign");
                String mysign = WapPayCore.getSignFromParams(param);
               /* if(StringUtils.isEmpty(sign) || StringUtils.isEmpty(mysign) || !AppPayCore.verify(param, sign)){
                    writer.write(String.format(result, "FAIL", "签名错误"));
                    writer.flush();
                    logger.info("微信-接收异步通知失败，签名错误");
                    return;
                }*/
                String resultCode = XmlUtil.parseXml(data, AppPayConfig.WECHAT_RESULT_CODE);
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
                    writer.write(String.format(result, "SUCCESS", "接收通知成功，订单不存在"));
                    writer.flush();
                    logger.info("微信-接受异步通知成功，不存在此订单ID：" + order.getOrderNumber());
                    return;
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
                    writer.write(String.format(result, "SUCCESS", "OK"));
                    writer.flush();
                    return;
                }else if(resultCode.contains(AppPayConfig.WECHAT_SUCCESS)){//成功接收通知(交易成功)

                    if(order.getStatus() != Constant.ORDER_FINISHED){//第一次处理通知
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
                    writer.write(String.format(result, "SUCCESS", "OK"));
                    writer.flush();
                    return;
                }else{
                    writer.write(String.format(result, "SUCCESS", "接收通知成功，但结果未知"));
                    logger.info("微信-接收异步通知成功，但结果未知");
                    writer.flush();
                    return;
                }
            }
        } catch (IOException e) {
            writer.write(String.format(result, "FAIL", "接收通知失败"));
            logger.error("微信-接受异步通知失败");
            writer.flush();
            e.printStackTrace();
            return;
        }finally {
            if(writer != null)
                writer.close();
        }
    }
    
    
    /**
     * 生成二维码，有效期两小时(微信支付模式二)
     * @param code_url
     * @param req
     * @param resp
     */
    @RequestMapping(value="/wap/scanCode/makeQrCode.do")
    public void makeQrCode(@RequestParam("code_url")String code_url, HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("image/png");
        try {
            code_url = URLDecoder.decode(code_url, AppPayConfig.WECHAT_INPUT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        QRCodeUtil.createQrCode(code_url, resp);
    }
}
