package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.*;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.*;

import org.apache.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.lang.Object;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zouyu on 2016/9/19.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    //日志实例
    private static final Logger logger = Logger.getLogger(UserController.class);

    //日志实例
    private static final Logger dblog = Logger.getLogger("dblog");

    @Autowired
    private IUserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ISaleService saleService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShareService shareService;

    /**
     * 获取手机验证码
     * 请求验证码标志： 1-注册 2-登陆 3-修改手机 4-找回密码 5-申请提现
     */
    @RequestMapping(value="/post_sendSecurityCode.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result post_sendSecurityCode(String mobile, Integer type) {
        if (mobile != null && !"".equals(mobile)) {
            if(type==null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码类型必填");
            }
            //验证手机号格式
            if(!ValidatorUtil.isMobile(mobile)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"无效手机号");
            }
            User user = userService.findByLoginId(mobile,Constant.REG_BY_MOBILE);
/*            if( type==Constant.SECURE_CODE_REG && user != null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"手机号已注册");
            }*/

//            if( type!=Constant.SECURE_CODE_LOGIN && user == null){
//                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"手机号未注册用户");
//            }
            //向指定手机发送校验码，按手机号+类型将校验码+时间戳放入缓存, 5分钟内有效
            String secureCode = userService.loadSecureCodeFromCache(mobile,type);
            if(secureCode==null || secureCode.isEmpty()) {
                secureCode = CodeGenerator.genSecurityCode();
                userService.syncSecureCodeToCache(mobile,type,secureCode);
            }
            switch (type) {
                case Constant.SECURE_CODE_REG:
                    SendSmsUtil.sendSmsForRegister(mobile, secureCode);
                    break;
                case Constant.SECURE_CODE_LOGIN:
                    SendSmsUtil.sendSmsForLogin(mobile, secureCode);
                    break;
                case Constant.SECURE_CODE_CHMOBILE:
                    SendSmsUtil.sendSmsForChangeMobile(mobile, secureCode);
                    break;
                case Constant.SECURE_CODE_CHPWD:
                    SendSmsUtil.sendSmsForFindPassword(mobile, secureCode);
                    break;
                case Constant.SECURE_CODE_CASHOUT:
                    SendSmsUtil.sendSmsForCashount(mobile, secureCode);
                    break;
                default:
            }
            return Result.BuildSuccessResult(null);
        } else {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "手机号不能为空");
        }
    }


    /*
     * 用户注册
     */
   /* @RequestMapping(value="/post_register.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result register(String mobile,String securityCode) {
        if(mobile == null || "".equals(mobile) ){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "手机号不能为空");
        }

        if(securityCode==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码不能为空");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile,Constant.SECURE_CODE_REG);
        if(secureCode==null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if(!securityCode.equals(secureCode)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码错误");
        }

        //验证手机号格式
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"无效手机号");
        }

        //检验手机号是否注册
        if(userService.findByLoginId(mobile,Constant.REG_BY_MOBILE) != null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"手机号已注册");
        }

        User user = userService.register(mobile);
        if(user!=null) {
            String passportId = CodeGenerator.genPassportId(user.getLoginID(),user.getLoginType());
            user.setPassportId(passportId);

            Map<String,Object> map = GroupUser.groupToMap(user);
            //TODO: 放入cookie中
            map.put("passportId", passportId);
            //将passportId放入缓存
            userService.syncUserToCache(passportId, user);
            userService.removeSecureCodeFromCache(user.getMobile(), Constant.SECURE_CODE_REG);
            logger.info("用户注册成功, mobile: "+mobile+", passportId: "+passportId);
            return Result.BuildSuccessResult(map);
        }else{
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器原因注册失败");
        }
    }*/

    /*
     * 用户登陆
     */
   /* @RequestMapping(value="/post_login_by_pwd.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result login_by_pwd(String mobile,String password) {
        if(mobile == null || "".equals(mobile) || password == null||"".equals(password)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"用户名或密码不能为空");
        }

        User user = userService.findByMobile(mobile);
        if(user == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"该手机号未注册");
        }

        if(!EncryptUtil.verifyPassword(password, user.getPassword())) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"密码不正确");
        }
        String passportId = CodeGenerator.genPassportId(mobile);
        user.setPassportId(passportId);
        Map<String,Object> map  = GroupUser.groupToMap(user);//根据用户的组返回不同的用户数据
        map.put("passportId", passportId);
        //将passportId与user信息存入缓存
        userService.syncUserToCache(passportId, user);
        logger.info("用户登录成功, mobile: "+mobile+", passportId: "+passportId);
        return Result.BuildSuccessResult(map);
    }*/

    /**
     * 用户验证码登陆
     */
    @RequestMapping(value="/post_login_by_securityCode.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result login_by_securityCode(String mobile,String securityCode,HttpServletRequest request) {
        if(mobile == null ||"".equals(mobile) || securityCode == null || "".equals(securityCode)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"用户名或校验码不能为空");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile,Constant.SECURE_CODE_LOGIN);
        if(secureCode==null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if(!securityCode.equals(secureCode)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码错误");
        }

        User user = userService.findByLoginId(mobile, (byte)0);
        if(user == null){
            user = userService.register(mobile);
            if(user == null){
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器异常注册失败");
            }
            logger.info("用户注册成功, mobile:"+mobile);
            MDC.put("userId",user.getId().toString());
            String userName = user.getNickName();
            if(StringUtils.isEmpty(userName))
                userName = user.getMobile();
            MDC.put("userName",userName);
            MDC.put("logActionType","3");
            dblog.info("用户"+userName+"从手机端注册成功");
        }
        if(user.getStatus()==1){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "用户已被停用，请联系管理员");
        }
        String passportId = CodeGenerator.genPassportId(user.getLoginID(),user.getLoginType());
        user.setPassportId(passportId);
        Map<String,Object> map = GroupUser.groupToMap(user);//根据用户的组返回不同的用户数据
        map.put("passportId", passportId);
        //将passportId与user信息存入缓存
        userService.syncUserToCache(passportId, user);
        userService.removeSecureCodeFromCache(mobile, Constant.SECURE_CODE_LOGIN);
        user.setLastLoginTime(new Date());
        userService.updateAtLogin(user);
        Integer _have = orderService.selectHaveBuy(user.getId());
        if(_have > 0){
            map.put("havebuy",1);
        }else {
            map.put("havebuy",0);
        }
        logger.info("用户登录成功, mobile: "+mobile+", passportId: "+passportId);
        MDC.put("userId",user.getId().toString());
        String userName = user.getNickName();
        if(StringUtils.isEmpty(userName))
            userName = user.getMobile();
        MDC.put("userName",userName);
        MDC.put("logActionType","1");
        dblog.info("用户"+userName+"从手机端登录成功");
        return Result.BuildSuccessResult(map);
    }

    /**
     * 查询用户
     * @param userCond 查询用户条件
     */
    @RequestMapping(value="/search.json",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result search(UserCondition userCond,String passportId){
        //TODO： 模糊查询
        Integer curUserId = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            User tmp = userService.loadUserFromCache(passportId);
            if(tmp!=null)  curUserId = tmp.getId();
        }
        List<User> list = userService.queryList(userCond,curUserId);
        if(list==null || list.size()==0)
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"没有相应用户");
        return Result.BuildSuccessResult(list);
    }

    /**
     * 根据group显示不同的用户
     */
    @RequestMapping(value="detail.json",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result detail(Integer userid, String passportId) {
        User user;
        if(userid != null) {
            Integer curUserId = null;
            if(StringUtils.isNotEmpty(passportId)){
                // 从缓存读个人信息
                User tmp = userService.loadUserFromCache(passportId);
                 if(tmp!=null)  curUserId = tmp.getId();
            }
            // 有id参数时从数据库查询用户信息
            user = userService.detail(userid,curUserId);
            if(user==null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"用户不存在");
            }
        }
        else {
            // 无id时返回当前登录用户信息
            if(passportId==null ||passportId.isEmpty()){
                return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED,"请登录");
            }
            // 从缓存读个人信息
            user = userService.loadUserFromCache(passportId);
            if(user==null){
                return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED,"请登录");
            }
        }
        Map<String,Object> map = GroupUser.groupToMap(user);
        if(user.getGroupId()==Constant.USER_GROUP_MODEL){
            Integer loginUserId = null;
            if(StringUtils.isNotEmpty(passportId)){
                User loginUser = userService.loadUserFromCache(passportId);
                if(loginUser!=null) loginUserId = loginUser.getId();
            }
            boolean isLiked = userService.isLikedByCurUser(user.getId(),Constant.LIKE_TYPE_GOD, loginUserId);
            map.put("isLikedByCurUser", isLiked);
        }

        UserAccount userAccount = userService.selectUserAccount(user);
        map.put("userAccount", userAccount);
        //今日收入
        Date date = DateUtil.getCurrentDateBegin();
        List<IncomeVO> incomes = saleService.getIncome(user.getId(), date);
        map.put("incomes", incomes);
        RecordQingdou _qingdou = new RecordQingdou();
        _qingdou.setUserId(user.getId());
        _qingdou.setCurrent(1);
        _qingdou.setRecType(Constant.QD_INCOME_SHARE);
        map.put("shareCounts",shareService.queryRecordCount(_qingdou));
        return Result.BuildSuccessResult(map);
    }

    /**
     * 修改用户的基本信息
     * @param itemName 字段名
     * @param itemValue 字段值
     */
    @RequestMapping(value="/post_edit_auth.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result edit(String itemName, String itemValue,HttpServletRequest request){
            User user = (User)request.getAttribute("user");
            int userId = user.getId();
        if(itemName == null || "".equals(itemName) || itemValue == null || "".equals(itemValue)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"修改信息不能为空");
        }
        if(itemName.equals("mobile")
            || itemName.equals("loginID")
            || itemName.equals("id")
            || itemName.equals("loginType")
            || itemName.equals("password")
            || itemName.equals("user_group")
            || itemName.equals("qingdou"))
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"接口无权限修改该字段");

        if(itemName.equals("birthday")){
            if(StringUtils.isEmpty(itemValue)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"生日不能为空");
            }
            if(itemValue.length()!=8){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"生日长度不正确");
            }

            if(!ValidatorUtil.isBirthday(itemValue)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"生日不合法");
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String date = format.format(new Date());
            if(Integer.valueOf(itemValue)-Integer.valueOf(date)>0){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"出生日期不能超过今天");
            }
        }
        else if(itemName.equals("nickname")){
            List<User> user1 = userService.findByNickName(itemValue);
            if(user1!=null && user1.size()>0){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"该昵称已存在");
            }
        }

        int item = userService.updateValue(itemName,itemValue,userId);
        if(item>0){
            String passportId = request.getParameter("passportId");
            User newUser = userService.detail(userId,null);
            userService.syncUserToCache(passportId,newUser);
            return Result.BuildSuccessResult(null);
        }else{
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"修改失败");
        }
    }

    /*
     * 修改密码接口
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
 /*   @RequestMapping(value="post_changePasswd_auth,json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result changePassword(String oldPassword, String newPassword,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        int userId = user.getId();
        if(oldPassword != null && !"".equals(oldPassword) && newPassword != null && !"".equals(newPassword)){
            if(!userService.checkPassword(userId, oldPassword)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"旧密码错误");
            }
            int i = userService.changePassword(userId,newPassword);
            if(i>0){
                return Result.BuildSuccessResult(null);
            }else{
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"修改失败");
            }
        }else{
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"密码不能为空");
        }
    }*/

    /**
     *认证申请
     */
    @RequestMapping(value="/post_apply_for_certified_auth.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result certify(Integer userGroup, String videoUrl,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        if(user.getGroupId()!=Constant.USER_GROUP_USER){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"已认证，不要重复提交");
        }

        int userId = user.getId();
        if(userGroup==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "userGroup不能为空");
        }
        if(userGroup==Constant.USER_GROUP_MODEL){
            if (videoUrl == null || "".equals(videoUrl)) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "认证申请提交失败,视频地址为空");
            }
        }
        else if(userGroup==Constant.USER_GROUP_PHOTOGRAPHER){
            videoUrl = null;
        }
        Integer status = userService.getCertifyStatus(userId);
        if(status!=null && status== Constant.USER_CERTIFY_PROCESSING){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"已申请认证，不要重复提交");
        } else if(status!=null && status== Constant.USER_CERTIFY_PASS){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"已通过认证，不要重复提交");
        }

        if(userService.certify(userId,userGroup,videoUrl)>0){
            return Result.BuildSuccessResult(null);
        }else{
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"认证申请提交失败");
        }
    }

    /*
     * 找回密码接口
     * @param mobile 手机号
     * @param securityCode 验证码
     * @param newPassword 新密码
     */
   /* @RequestMapping(value="/post_findPasswd.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result findPassword(String mobile, String securityCode, String newPassword){
        if(mobile == null || "".equals(mobile)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "手机号不能为空");
        }
        //验证手机号格式
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"无效手机号");
        }
        User user = userService.findByMobile(mobile);
        if(user != null){
            if(newPassword == null || "".equals(newPassword)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "新密码不能为空");
            }

            if(securityCode==null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码不能为空");
            }

            //验证securityCode是否过期
            String secureCode = userService.loadSecureCodeFromCache(mobile,Constant.SECURE_CODE_CHPWD);
            if(secureCode==null || secureCode.isEmpty()) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码已过期，请重新生成");
            }

            //验证securityCode是否正确
            if(!securityCode.equals(secureCode)){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码错误");
            }
            userService.changePassword(user.getId(), newPassword);
            userService.removeSecureCodeFromCache(mobile,Constant.SECURE_CODE_CHPWD);
            logger.info("找回密码成功, mobile: "+mobile);
            return Result.BuildSuccessResult(null);
        }else{
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"该用户不存在");
        }
    }*/

    /**
     * 三方登录
     * @param openId 用户唯一标识
     * @param type 登录类型:1.QQ、2.微信、3.微博
     * @param imgUrl 三方授权返回的头像url，可以为空
     * @param nickName 三方授权返回的昵称，可以为空
     */
    @RequestMapping(value="/post_thirdlogin.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result thirdlogin(String openId,Byte type,String imgUrl, String nickName,HttpServletRequest request){
        if(openId == null || "".equals(openId)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "用户授权Id不能为空");
        }
        if(type == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "三方授权类型不能为空");
        }
        //String  unionids = request.getParameter("unionid");
        User user = userService.findByLoginId(openId, type);
        if(user == null){
        	//dblog.info(thirdStr+"用户"+userName+"从手机端第三方注册成功");
        	//opendid没有注册过用户
        	//User userunion = userService.findByLoginId(unionids, type);
        	//if(userunion==null){
            user = userService.register(openId,type,imgUrl, nickName);
            if(user == null){
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器异常注册失败");
            }
            MDC.put("userId",user.getId().toString());
            String userName = user.getNickName();
            if(StringUtils.isEmpty(userName))
                userName = user.getMobile();
            MDC.put("userName",userName);
            MDC.put("logActionType","3");
            String thirdStr = "";
            if(type==1)
                thirdStr = "QQ";
            else if(type==2)
                thirdStr = "微信";
            else if(type==3)
                thirdStr = "微博";
            dblog.info(thirdStr+"用户"+userName+"从手机端第三方注册成功");
        	//}
            
        }
        logger.info("======= "+user.getLoginID()+"====="+user.getLoginType());
        if(user.getStatus()==1){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "用户已被停用，请联系管理员");
        }
        String passportId = CodeGenerator.genPassportId(user.getLoginID(),user.getLoginType());
        user.setPassportId(passportId);
        Map<String, Object> map = GroupUser.groupToMap(user);//根据用户的组返回不同的用户数据
        map.put("passportId", passportId);
        //将passportId与user信息存入缓存
        userService.syncUserToCache(passportId, user);
        user.setLastLoginTime(new Date());
        user.setUnionid(openId);
        userService.updateAtLogin(user);
        logger.info("通过第三方登录成功,  passportId: "+passportId);
        MDC.put("userId",user.getId().toString());
        String userName = user.getNickName();
        if(StringUtils.isEmpty(userName))
           userName = user.getMobile();
        if(!StringUtils.isEmpty(userName)){
        MDC.put("userName",userName);
        }
        MDC.put("logActionType","1");
        String thirdStr = "";
        if (type == 1) {
            thirdStr = "QQ";
            map.put("loginType", Constant.LOGIN_TYPE_QQ);
        } else if (type == 2) {
            thirdStr = "微信";
            map.put("loginType", Constant.LOGIN_TYPE_WECHAT);
        } else if (type == 3) {
            thirdStr = "微博";
            map.put("loginType", Constant.LOGIN_TYPE_WEIBO);
        }
        Integer _have = orderService.selectHaveBuy(user.getId());
        if(_have > 0){
            map.put("havebuy",1);
        }else {
            map.put("havebuy",0);
        }
       // request.setAttribute("user", user);//缓存用户信息
        dblog.info(thirdStr + "用户" + userName + "从手机端第三方登录成功");
        return Result.BuildSuccessResult(map);
    }

    /*
     * 三方授权登录，首次绑定手机号并更新用户信息
     * @param openId 用户唯一标识
     * @param type 登录类型:1.QQ、2.微信、3.微博
     * @param mobile 绑定的手机号
     * @param securityCode 验证码
     * @param imgUrl 三方授权返回的头像url，可以为空
     * @param nickName 三方授权返回的昵称，可以为空
     */
   /* @RequestMapping(value="/post_thirdlogin_bindMobile.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result thirdlogin_bindMobile(String openId, Byte type, String mobile,String securityCode, String imgUrl, String nickName){
        if(openId == null || "".equals(openId)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "用户授权Id不能为空");
        }
        if(type == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "三方授权类型不能为空");
        }
        if(mobile == null || "".equals(mobile)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "手机号不能为空");
        }
        //验证手机号格式
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"无效手机号");
        }
        if(securityCode==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码不能为空");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile,Constant.SECURE_CODE_LOGIN);
        if(secureCode==null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if(!securityCode.equals(secureCode)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码错误");
        }
        User user = userService.findByOpenId(openId, type);
        if(user!=null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"此用户已授权，无需绑定直接登陆");
        }

        user = userService.findByMobile(mobile);
        if(user==null){
            // 用户不存在，需要注册新用户
            user = userService.register(openId, type, mobile, imgUrl, nickName);
            if(user == null){
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器原因注册失败");
            }
        }
        else{
            // 用户已存在，直接更新信息
            user.setThirdPartyLoginID(openId);
            user.setThirdPartyLoginType(type);
            if(imgUrl!=null && !imgUrl.isEmpty()){
                user.setFace(imgUrl);
            }
            if(nickName!=null && !nickName.isEmpty()){
                user.setNickName(nickName);
            }
            userService.updateFrom3party(user);
        }
        String passportId = CodeGenerator.genPassportId(user.getMobile());
        user.setPassportId(passportId);
        Map<String, Object> map = GroupUser.groupToMap(user);//根据用户的组返回不同的用户数据
        map.put("passportId", passportId);
        //将passportId与user信息存入缓存
        userService.syncUserToCache(passportId, user);
        logger.info("通过第三方注册成功,  passportId: "+passportId);
        return Result.BuildSuccessResult(map);
    }*/


    /**
     * 给女神点赞/取消点赞
     * @param godUserId 女神id
     */
    @RequestMapping(value="/post_god_like_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result godLike(Integer godUserId,HttpServletRequest request){
        if(godUserId==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "女神id不能为空");
        }

        Integer likeCount = userService.selectGodLikeCount(godUserId);
        if(likeCount==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "当前查询的女神不存在");
        }

        GodViewLikeVO likeVO =  new GodViewLikeVO();
        User user = (User)request.getAttribute("user");
        int userId = user.getId();
        likeVO.setUserId(userId);
        likeVO.setLikeId(godUserId);
        likeVO.setLikeType(Constant.LIKE_TYPE_GOD);
        //TODO: 改用redis缓存点赞状态，定时批量入库
        //查询是否已赞
        GodViewLikeVO vo = userService.selectIsLikeGod(likeVO);
        try {
            if (vo != null) {
                //取消点赞
                userService.giveDownGod(vo);
                likeCount--;
            } else {
                //点赞
                likeVO.setCreateTime(new Date());
                userService.giveUpGod(likeVO);
                likeCount++;
            }
            return Result.BuildSuccessResult(likeCount);
        }catch(Exception e){
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
    }

    /**
     * 修改手机号
     * @param mobile 新手机号
     * @param securityCode 验证码

     */
    @RequestMapping(value="/post_changeMobile_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result changeMobile(String mobile, String securityCode, HttpServletRequest request){
        User user = (User)request.getAttribute("user");

        if(mobile == null || "".equals(mobile)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "新手机号不能为空");
        }
        //验证手机号格式
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"无效手机号");
        }
        if (userService.findByMobile(mobile) > 0) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "新手机号已经注册");
        }

        if(securityCode==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码不能为空");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile,Constant.SECURE_CODE_CHMOBILE);
        if(secureCode==null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if(!securityCode.equals(secureCode)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"验证码错误");
        }

        String oldMobile = user.getMobile();
        userService.updateMobile(mobile, user.getId());
        user.setMobile(mobile);
        if(user.getLoginType()==Constant.REG_BY_MOBILE) {
            user.setLoginID(mobile);
        }

        //将passportId与user信息存入缓存
        userService.syncUserToCache(user.getPassportId(), user);
        userService.removeSecureCodeFromCache(mobile, Constant.SECURE_CODE_CHMOBILE);
        userService.removeAllSecureCodeFromCache(oldMobile);
        logger.info("修改手机号成功, 手机变更为: "+mobile+", passportId: "+user.getPassportId());
        return Result.BuildSuccessResult(null);
    }

    @RequestMapping("/change_group.json")
    @ResponseBody
    public Result change_group(Integer group){
        if(Constant.DEBUG_MODE) {
            User user = userService.findByLoginId("18611640071", (byte) 0);
            userService.updateValue("user_group", group.toString(), user.getId());
        }
       return Result.BuildSuccessResult(null);
    }

    /**
     * 关注他人/取消关注他人
     * @param userId 关注的用户id
     */
    @RequestMapping(value="/post_focus_user_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result focusUser(Integer userId,HttpServletRequest request){
        if(userId==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "关注用户id不能为空");
        }

        User focusUser = userService.detail(userId,null);
        if(focusUser==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "当前关注的用户不存在");
        }

        User user = (User)request.getAttribute("user");
        int curUserId = user.getId();
        //查询是否已赞
        Integer count = userService.getFocusCount(curUserId, userId);
        try {
            if (count >0) {
                //取消关注
                userService.saveUnfocusUser(curUserId,userId);
                return Result.BuildSuccessResult("取消关注成功");
            } else {
                //关注
                userService.saveFocusUser(curUserId,userId);
                return Result.BuildSuccessResult("关注成功");
            }

        }catch(Exception e){
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
    }


    @RequestMapping(value="/post_edit_receiver_auth.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result editReceiver(User userVO,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        int userId = user.getId();

        if(StringUtils.isNotEmpty(userVO.getPostCode()) && userVO.getPostCode().length()!=6){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"邮政编码长度不正确");
        }

        userVO.setId(userId);

        int item = userService.updateReceiver(userVO);
        if(item>0){
            String passportId = request.getParameter("passportId");
            User newUser = userService.detail(userId,null);
            userService.syncUserToCache(passportId,newUser);
            return Result.BuildSuccessResult("修改收货信息成功");
        }else{
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"修改收货信息失败");
        }
    }

    @RequestMapping(value="/post_supporter_rank_auth.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result supporterRank(Integer pageNo, Integer pageSize,HttpServletRequest request){
        User user = (User)request.getAttribute("user");

        if(user.getGroupId()!=Constant.USER_GROUP_MODEL || user.getGroupId()!=Constant.USER_GROUP_PHOTOGRAPHER){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"登陆用户不是认证用户");
        }

        List<ActivitySupporter> list= userService.querySupporterTotalRank(user.getId(),user.getGroupId(),pageNo,pageSize);
        if(list!=null && list.size()>0){
            return Result.BuildSuccessResult(list);
        }else{
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"无支持者");
        }
    }

    /**
     * 我的活动
     * @return
     */
    @RequestMapping(value = "/post_myActivity_auth.json",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result myActivity(Integer pageNo,Integer pageSize,HttpServletRequest request){
        User user = (User)request.getAttribute("user");

        if(user.getGroupId()!=Constant.USER_GROUP_MODEL){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"登陆用户不是认证女神");
        }
        List<Activity> list = activityService.queryByModelId(user.getId(),pageNo,pageSize);
        if(list==null || list.size() == 0){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"没有参与活动");
        }
        return Result.BuildSuccessResult(list);
    }



    /**
     * 手机号登陆
     *
     * @param password 密码
     * @param mobile   手机号
     * @return flag 是否已有账号
     */
    @RequestMapping(value = "/post_login_by_mobile.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result login_by_mobile(String mobile, String password, HttpServletRequest request) {
        if (mobile == null || "".equals(mobile) || password == null || "".equals(password)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "手机号或密码不能为空");
        }
        if (!ValidatorUtil.isMobile(mobile)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "无效手机号");
        }

        User _user = userService.findByLoginId(mobile, Constant.REG_BY_MOBILE);
        User user_ = userService.findByLoginId(mobile, Constant.REG_BY_ACCOUNT);
        Map<String, Object> map = new HashMap<>();
        User user = new User();
        if (_user != null || user_ != null) {
            if (_user != null) {
                user = _user;
            } else {
                user = user_;
            }

            if (!EncryptUtil.comparePasswords(user.getPassword(), password)) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "密码不正确");
            }
            map = GroupUser.groupToMap(user);//根据用户的组返回不同的用户数据
            //老用户，验证登录
            user.setLastLoginTime(new Date());
            userService.updateAtLogin(user);//更新登录信息
            Integer _have = orderService.selectHaveBuy(user.getId());
            if(_have > 0){
                map.put("havebuy",1);
            }else {
                map.put("havebuy",0);
            }
            if (user.getStatus() == 0) {
                String passportId = CodeGenerator.genPassportId(mobile, Constant.REG_BY_MOBILE);
                user.setPassportId(passportId);
                map.put("flag", true);
                map.put("passportId", passportId);
                map.put("loginType", Constant.LOGIN_TYPE_MOBILE);
                userService.syncUserToCache(passportId, user);
                logger.info("用户登录成功, mobile: " + mobile + ", passportId: " + passportId);
                return Result.BuildSuccessResult(map);
            } else {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "账户处于被锁定等非正常状态");
            }
        }
        //新用户
        map.put("mobile", mobile);
        map.put("password", password);
        map.put("flag", false);
        return Result.BuildSuccessResult(map);
    }

    /**
     * 手机号注册
     *
     * @param password     密码
     * @param mobile       手机号
     * @param securityCode 验证码
     */
    @RequestMapping(value = "/post_register_byMobile.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result register(String mobile, String password, String securityCode) {
        if (mobile == null || "".equals(mobile) || password == null || "".equals(password)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "手机号或密码不能为空");
        }

        if (!ValidatorUtil.isMobile(mobile)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "无效手机号");
        }

        if(!ValidatorUtil.isPassword(password)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "无效密码");
        }

        if (securityCode == null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码不能为空");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile, Constant.SECURE_CODE_REG);
        if (secureCode == null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if (!securityCode.equals(secureCode)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码错误");
        }

        //检验手机号是否注册
        if (userService.findByLoginId(mobile, Constant.REG_BY_MOBILE) != null||userService.findByLoginId(mobile, Constant.REG_BY_ACCOUNT) != null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "手机号已注册，请直接登录。");
        }
        User _user = new User();
        _user.setPassword(EncryptUtil.createDbPassword(password));
        _user.setLoginID(mobile);
        _user.setLoginType((byte) 0);
        _user.setMobile(mobile);
        _user.setGroupId(Constant.USER_GROUP_USER);//用户第一次注册默认为普通用户
        _user.setRegisterTime(new Date());
        _user.setStatus((byte) 0);
        User user = userService.register(_user);
        if (user != null) {
            String passportId = CodeGenerator.genPassportId(user.getLoginID(), user.getLoginType());
            user.setPassportId(passportId);

            Map<String, Object> map = GroupUser.groupToMap(user);
            //TODO: 放入cookie中
            map.put("passportId", passportId);
            //将passportId放入缓存
            userService.syncUserToCache(passportId, user);
            userService.removeSecureCodeFromCache(user.getMobile(), Constant.SECURE_CODE_REG);
            logger.info("用户注册成功, mobile: " + mobile + ", passportId: " + passportId);
            return Result.BuildSuccessResult(map);
        } else {
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器原因注册失败");
        }
    }

    /**
     * 邮箱账号登陆
     * @param email 邮箱
     * @param password 密码
     */
    @RequestMapping(value = "/post_login_by_email.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result login_by_email(String email, String password, HttpServletRequest request) {
        if (email == null || "".equals(email) || password == null || "".equals(password)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "邮箱账号或密码不能为空");
        }
        if (!ValidatorUtil.isEmail(email)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "无效邮箱");
        }

        User user = userService.findByLoginId(email, Constant.REG_BY_ACCOUNT);
        Map<String, Object> map = new HashMap<>();
        if (user != null) {
            //老用户，验证登录
            if (!EncryptUtil.comparePasswords(user.getPassword(), password)) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "密码不正确");
            }

            map = GroupUser.groupToMap(user);//根据用户的组返回不同的用户数据
            map.put("ismobile", true);
            if(user.getMobile() == null || "".equals( user.getMobile())){
                map.put("ismobile", false);
            }
            user.setLastLoginTime(new Date());
            userService.updateAtLogin(user);//更新登录信息
            if (user.getStatus() == 0) {
                String passportId = CodeGenerator.genPassportId(email, Constant.REG_BY_ACCOUNT);
                user.setPassportId(passportId);
                map.put("flag", true);
                map.put("passportId", passportId);
                map.put("loginType", Constant.LOGIN_TYPE_EMAIL);
                Integer _have = orderService.selectHaveBuy(user.getId());
                if(_have > 0){
                    map.put("havebuy",1);
                }else {
                    map.put("havebuy",0);
                }
                userService.syncUserToCache(passportId, user);
                logger.info("用户登录成功, email: " + email + ", passportId: " + passportId);
                return Result.BuildSuccessResult(map);
            } else {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "账户处于被锁定等非正常状态");
            }
        }
        //新用户
        map.put("email", email);
        map.put("password", password);
        map.put("flag", false);
        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "邮箱账号或密码错误");
    }

    /**
     * 修改密码
     *
     * @param password     新密码
     * @param mobile       手机号
     * @param securityCode 验证码
     */
    @RequestMapping(value = "/post_changePassowrd_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result changePassowrd(String password, String mobile, String securityCode, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        if (password == null || "".equals(password)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "新密码不能为空");
        }
        //验证密码格式
        if (!ValidatorUtil.isPassword(password)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "无效密码");
        }

        if (securityCode == null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码不能为空");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile, Constant.SECURE_CODE_CHPWD);
        if (secureCode == null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if (!securityCode.equals(secureCode)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码错误");
        }

        if (user != null) {
            try {
                userService.changePassword(user.getId(), password);
                user.setPassword(password);
                //将passportId与user信息存入缓存
                userService.syncUserToCache(user.getPassportId(), user);
                userService.removeSecureCodeFromCache(mobile, Constant.SECURE_CODE_CHPWD);
                userService.removeAllSecureCodeFromCache(mobile);
                logger.info("修改密码成功, passportId: " + user.getPassportId());
                return Result.BuildSuccessResult(null);
            } catch (Exception e) {
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器原因修改失败");
            }
        }
        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "请先登录");
    }
      /**
       * 点下一步接口验证手机验证码
       * @param mobile
       * @param securityCode
       * @param type
       * @return
       */
    @RequestMapping(value = "/post_verifySecurityCode.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result verifySecurityCode(String mobile,String securityCode,Integer type){
        if (securityCode == null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码不能为空");
        }
        
        if(type==null){
        	type =	Constant.SECURE_CODE_REG;//1为注册
        }
        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile, type);
        if (secureCode == null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if (!securityCode.equals(secureCode)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码错误");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("success","true");
        return Result.BuildSuccessResult(map);
    }

    /**
     * 找回密码
     *
     * @param mobile       手机号
     * @param securityCode 验证码
     */
    @RequestMapping(value = "/post_findPassowrd.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result findPassowrd(String mobile, String securityCode, HttpServletRequest request) {

        if (securityCode == null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码不能为空");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile, Constant.SECURE_CODE_CHPWD);
        if (secureCode == null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if (!securityCode.equals(secureCode)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码错误");
        }

        User user_ = userService.findByLoginId(mobile, Constant.REG_BY_MOBILE);
        User _user = userService.findByLoginId(mobile, Constant.REG_BY_ACCOUNT);
        Map<String, Object> map = new HashMap<>();
        if (user_ == null && _user == null) {
            map.put("flag", false);
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "该手机号未注册过，请前往注册");
        }
        map.put("flag", true);
        map.put("mobile", mobile);
        return Result.BuildSuccessResult(map);
    }

    /**
     * 找回密码
     *
     * @param password 新密码
     * @param mobile   手机号
     */
    @RequestMapping(value = "/post_refindPassowrd.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result refindPassowrd(String password, String mobile, HttpServletRequest request) {

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile, Constant.SECURE_CODE_CHPWD);
        if (secureCode == null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码已过期，请重新生成");
        }

        if (password == null || "".equals(password)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "新密码不能为空");
        }
        //验证密码格式
        if (!ValidatorUtil.isPassword(password)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "无效密码");
        }
        Map<String, Object> map = new HashMap<>();
        User user_ = userService.findByLoginId(mobile, Constant.REG_BY_MOBILE);
        User _user = userService.findByLoginId(mobile, Constant.REG_BY_ACCOUNT);
        User user;
        if (_user != null || user_ != null) {
            if (_user != null) {
                user = _user;
            } else {
                user = user_;
            }
            try {
                userService.changePassword(user.getId(), password);
                map.put("flag", true);
                map.put("msg", "密码修改成功");
                logger.info("修改密码成功, passportId: " + user.getPassportId());
                return Result.BuildSuccessResult(map);
            } catch (Exception e) {
                map.put("flag", false);
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器原因修改失败");
            }

        }
        map.put("flag", false);
        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "该手机号未注册过，请前往注册");
    }

    /**
     * 已登录用户验证手机
     *
     * @param mobile       手机号
     * @param securityCode 验证码
     */
    @RequestMapping(value = "/post_bindMobile_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result bindMobile(String mobile, String securityCode, HttpServletRequest request) {

        if (mobile == null || "".equals(mobile) || securityCode == null || "".equals(securityCode)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "手机号或验证码不能为空");
        }

        if (!ValidatorUtil.isMobile(mobile)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "无效手机号");
        }

        //验证securityCode是否过期
        String secureCode = userService.loadSecureCodeFromCache(mobile, Constant.SECURE_CODE_CHMOBILE);
        if (secureCode == null || secureCode.isEmpty()) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码已过期，请重新生成");
        }

        //验证securityCode是否正确
        if (!securityCode.equals(secureCode)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "验证码错误");
        }

        User user = (User) request.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        if(user.getMobile().equals(mobile)){
            map.put("flag", true);
            map.put("mobile", mobile);
            return Result.BuildSuccessResult(map);
        }
        map.put("flag", false);
        map.put("mobile", mobile);
        return Result.BuildSuccessResult(map);
    }

    @RequestMapping(value = "/post_photoCollection.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getPhotoCollectionByUserId(HttpServletRequest req,int pageNo,int pageSize){
        String passportId = req.getParameter("passportId");
        if(StringUtils.isTrimEmpty(passportId)){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"未登录");
        }
        User user = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            user = userService.loadUserFromCache(passportId);
        }
        List<UsersCollection> list = null;
        try{
            list = userService.getPhotoCollectionByUserId(user.getId(),pageNo,pageSize);
        }catch (Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
        return Result.BuildSuccessResult(list);
    }
}
