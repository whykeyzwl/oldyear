package com.wishes.yearOld.controller;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.DateUtil;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/account")
public class SaleController {

    /**
     * 日志实例
     */
    private static final Logger logger = Logger.getLogger(SaleController.class);

    @Autowired
    private ISaleService saleService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IUserService userService;
    /**
     * 根据用户获取当前用户消费记录
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/post_latest_records_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getUserTradeRecordList(HttpServletRequest request,
                                         @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User)request.getAttribute("user");
        int userid = user.getId();
        UserRecord params = new UserRecord();
        params.setUserid(userid);
        params.setPageNo(pageNo);
        params.setPageSize(pageSize);
        List<UserRecord> userRecordList = saleService.queryUserRecord(params);
        //对列表中的数据进行数据补充
        if (userRecordList != null && userRecordList.size() > 0) {
            for (UserRecord userRecord : userRecordList) {
                saleService.userRecordReplenish(userRecord);
            }
        }
        return Result.BuildSuccessResult(userRecordList);
    }


    @RequestMapping(value = "/post_trading_particulars_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  Result getUserTradeRecordDetials(@RequestParam(value = "tradeID", defaultValue = "0") String id) {
        if(id==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"交易记录id不能为空");
        }
        UserRecord userRecord = saleService.loadUserRecord(id);
        if(userRecord==null)
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"交易记录不存在");
        saleService.userRecordReplenish(userRecord);
        return Result.BuildSuccessResult(userRecord);
    }

    /**
     * 根据订单ID获取当前用户的交易信息
     * 不包含青豆交易的记录
     */
    @RequestMapping(value = "/post_trading_order_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getUserRecordByOrderId(HttpServletRequest request,
                                         @RequestParam(value = "orderId", defaultValue = "0")Integer orderId){
        if(orderId == null && orderId == 0){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "订单id不能为空");
        }
        User user = (User) request.getAttribute("user");
        UserRecord record = new UserRecord();
        record.setOrderId(orderId);
        record.setUserid(user.getId());
        UserRecord _record = saleService.loadUserRecordByOrderId(record);
        if(_record == null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "交易记录不存在");
        }
        saleService.userRecordReplenish(_record);
        return Result.BuildSuccessResult(_record);
    }

    /**
     * 获取用户当日收入
     *
     * @return
     */
    @RequestMapping(value = "/post_income_today_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getUserIncome(HttpServletRequest request) {
        User user = (User)request.getAttribute("user");
        int userid = user.getId();
        Date date = DateUtil.getCurrentDateBegin();
        List<IncomeVO> incomes = saleService.getIncome(userid, date);
        if(incomes==null || incomes.size()==0){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"收入记录不存在");
        }
        return Result.BuildSuccessResult(incomes);
    }

    /**
     * 用户提取现金
     * @param amount 提现金额
     * @param passportId
     * @param accountType 提现类型
     * @param securityCode 验证码
     * @param mobile 手机号
     * @return
     */
    @RequestMapping(value = "/post_apply_for_cashout.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    Result applyCashout(HttpServletRequest request, @RequestParam(value = "passportId", defaultValue = "") String passportId,@RequestParam(value = "amount", defaultValue = "0") BigDecimal amount, @RequestParam(value = "accountType", defaultValue = "0") Integer accountType, String securityCode, String mobile) {
        if(securityCode==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码不能为空");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile, Constant.SECURE_CODE_CHMOBILE);
        if(secureCode==null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if(!securityCode.equals(secureCode)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码错误");
        }
        if (amount.doubleValue() <= 0) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "提取现金不能为0！");
        }
        User user = userService.loadUserFromCache(passportId);
       // User user = (User) request.getAttribute("user");
        if (user.getZhifubao() == null || user.getZhifubao().isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "用户提现账户不能为空！");
        }
        UserAccount userAccount = userService.selectUserAccount(user);//TODO 收入什么时候insert呢？
        if (userAccount == null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "数据异常！");
        }
        Map<String, Object> map_ = saleService.NowBalanceCashout(userAccount);
        BigDecimal nowBalance = new BigDecimal(map_.get("nowBalance").toString());//当前可用余额
        if (nowBalance.compareTo(amount) == -1) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "提现金额超出当前可用余额！");
        }
        ApplyCashout applyCashout = new ApplyCashout();
        applyCashout.setAccountNum(user.getZhifubao());
        applyCashout.setApplyTime(new Date());
        applyCashout.setStatus(Constant.CASHOUT_INIT);
        applyCashout.setPayStatus(Constant.CASHOUT_UNPAY);
        applyCashout.setUserGroup(user.getGroupId().byteValue());
        applyCashout.setUserId(user.getId());
        applyCashout.setCashout(amount);
        applyCashout.setBatchNo("1");//TODO 批次号
        try {
            saleService.insertApplyCashout(applyCashout);//提现申请记录
            return Result.BuildSuccessResult("提现申请提交成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器异常");
        }

    }

    /**
     * 获取订单列表
     * @param request
     * @param type（1：全部 2：套图 3：旅拍）
     * @return
     */
    @RequestMapping(value = "/post_order_list_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  Result postOrderList(HttpServletRequest request,@RequestParam(value = "type", defaultValue = "0") Integer type,
                                 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User)request.getAttribute("user");
        int userid = user.getId();
        if(type==null|| type.intValue()==0){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"type参数不正确");
        }
        List<OrderListVO> orderListVOList = new ArrayList<OrderListVO>();
        Order params = new Order();
        params.setBuyerId(userid);
        params.setPageSize(pageSize);
        params.setPageNo(pageNo);
        if (type.intValue() == 2) {
            params.setOrderType((byte)1);
            orderListVOList = orderService.queryOrderList(params);
        }else if(type.intValue() == 3){
           params.setOrderType((byte)2);
           orderListVOList = orderService.queryOrderList(params);
        }else if (type.intValue() == 1) {
           orderListVOList = orderService.queryOrderList(params);
        }else {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"type参数不正确");
        }
        return Result.BuildSuccessResult(orderListVOList);
    }

    /**
     * 获取订单列表包含青豆支付
     * @param request
     * @param type（1：全部 2：套图 3：旅拍）
     * @return
     */
    @RequestMapping(value = "/post_order_list_authQd.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  Result postOrderQdList(HttpServletRequest request,@RequestParam(value = "type", defaultValue = "0") Integer type,
                                 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User)request.getAttribute("user");
        int userid = user.getId();
        if(type==null|| type.intValue()==0){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"type参数不正确");
        }
        List<OrderListVO> orderListVOList = new ArrayList<OrderListVO>();
        Order params = new Order();
        params.setBuyerId(userid);
        params.setPageSize(pageSize);
        params.setPageNo(pageNo);
        if (type.intValue() == 2) {
            params.setOrderType((byte)1);
            orderListVOList = orderService.queryOrderQdList(params);
        }else if(type.intValue() == 3){
           params.setOrderType((byte)2);
           orderListVOList = orderService.queryOrderQdList(params);
        }else if (type.intValue() == 1) {
           orderListVOList = orderService.queryOrderQdList(params);
        }else {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"type参数不正确");
        }
        return Result.BuildSuccessResult(orderListVOList);
    }
    /**
     * 获取订单列表包含青豆支付数据大小
     * @param request
     * @param type（1：全部 2：套图 3：旅拍）
     * @return
     */
    @RequestMapping(value = "/post_order_list_authQdSize.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  Result postOrderQdListSize(HttpServletRequest request,@RequestParam(value = "type", defaultValue = "0") Integer type,
                                 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User)request.getAttribute("user");
        int userid = user.getId();
        if(type==null|| type.intValue()==0){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"type参数不正确");
        }
        Integer orderListVOList=0 ;
        Order params = new Order();
        params.setBuyerId(userid);
        if (type.intValue() == 2) {
            params.setOrderType((byte)1);
            orderListVOList = orderService.queryOrderQdListSize(params);
        }else if(type.intValue() == 3){
           params.setOrderType((byte)2);
           orderListVOList = orderService.queryOrderQdListSize(params);
        }else if (type.intValue() == 1) {
           orderListVOList = orderService.queryOrderQdListSize(params);
        }else {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"type参数不正确");
        }
        return Result.BuildSuccessResult(orderListVOList);
    }

    /**
     * 收入管理
     *
     */
    @RequestMapping(value = "/post_income.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result postIncome(HttpServletRequest request, @RequestParam(value = "passportId", defaultValue = "") String passportId) {
        User user = userService.loadUserFromCache(passportId);
        Map<String, Object> map = new HashMap<>();
        UserAccount userAccount = userService.selectUserAccount(user);
        if (userAccount == null) {
            map.put("flag",false );//无收入
            return Result.BuildSuccessResult(map);
        }
        map.put("userAccount", userAccount);
        map.put("account", userAccount.getAmount());
        //今日收入
        Date date = DateUtil.getCurrentDateBegin();
        List<IncomeVO> incomes = saleService.getIncome(user.getId(), date);
        if (incomes == null || incomes.size() == 0) {
            map.put("incomes", 0);
        }
        //提现状态
        Map<String, Object> map_ = saleService.NowBalanceCashout(userAccount);
        map.putAll(map_);
        map.put("flag",true);
        map.put("incomes", incomes);//今日收入
        return Result.BuildSuccessResult(map);
    }
}
