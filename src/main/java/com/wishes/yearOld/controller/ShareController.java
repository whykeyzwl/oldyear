package com.wishes.yearOld.controller;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.model.RecordQingdou;
import com.wishes.yearOld.model.ResponseCode;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.service.IUserService;
import com.wishes.yearOld.service.ShareService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2016/12/22
 * Description:
 */

@Controller
@RequestMapping("/invite")
public class ShareController {

    @Autowired
    ShareService shareService;

    @Autowired
    IUserService userService;

    /**
     * 返回当天剩余时间
     *
     * @param passportId
     */
    @RequestMapping(value = "/getRemainTime.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getRemainTime(@RequestParam(value = "passportId", defaultValue = "") String passportId) {
        Map<String, Object> map = new HashMap<>();
        if (passportId != null && !"".equals(passportId)) {
            User user = userService.loadUserFromCache(passportId);
            RecordQingdou recordQingdou_ = new RecordQingdou();
            recordQingdou_.setUserId(user.getId());
            RecordQingdou recordQingdou = shareService.queryRecordQingdou(recordQingdou_);
            if (recordQingdou != null) {
                map.put("lastTime", recordQingdou.getCreateTime());
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long remain = cal.getTimeInMillis() - System.currentTimeMillis();
        long hour = remain / (1000L * 60 * 60);//计算差多少小时
        long min = remain % (1000L * 60 * 60) / (1000L * 60);//计算差多少分钟
        long sec = remain % (1000L * 60) / 1000L;//计算差多少秒//输出结果
        String remainTime = hour + "小时" + min + "分钟" + sec + "秒";
        map.put("remainTime", remainTime);
        return Result.BuildSuccessResult(map);
    }


    /**
     * 返回当天已邀请次数及下一次邀请可得青豆值
     */
    @RequestMapping(value = "/post_getTodayInvite_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getTodayShare(HttpServletRequest request) throws ParseException {
        User user = (User) request.getAttribute("user");
        RecordQingdou recordQingdou_ = new RecordQingdou();
        recordQingdou_.setUserId(user.getId());
        recordQingdou_.setRecType(Constant.QD_INCOME_INVITE);
        RecordQingdou recordQingdou = shareService.queryRecordQingdou(recordQingdou_);
        boolean i = true;//是否可以分享
        Date time = new Date();//当前剩余时间
        String remainTime = "";
        RecordQingdou record = new RecordQingdou();
        record.setUserId(user.getId());
        record.setRecType(Constant.QD_INCOME_INVITE);
        record.setCreateTime(new Date());
        if (recordQingdou != null) {
            //曾经分享|邀请过
            i = shareService.isHourLater(recordQingdou.getCreateTime(), 2);
            time = recordQingdou.getCreateTime();
            if (!i) {//距上次不超过两个小时
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                cal.add(Calendar.HOUR_OF_DAY, 2);
                long remain = cal.getTimeInMillis() - System.currentTimeMillis();
                long hour = remain / (1000L * 60 * 60);//计算差多少小时
                long min = remain % (1000L * 60 * 60) / (1000L * 60);//计算差多少分钟
                long sec = remain % (1000L * 60) / 1000L;//计算差多少秒//输出结果
                remainTime = hour + "小时" + min + "分钟" + sec + "秒";
                i = false;
            }
        }
        Integer count = shareService.getTodayInviteTimes(user);//查询当前邀请记录
        int qingdou = 0;
        if (count == 0)
            qingdou = 10;
        else if (count == 1)
            qingdou = 20;
        else if (count == 2)
            qingdou = 30;
        else {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "今日邀请已经达到上限");
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("qingdouNextInvite", qingdou);
        ret.put("inviteCount", count);
        ret.put("flag", i);
        ret.put("time", remainTime);
        return Result.BuildSuccessResult(ret);
    }

    /**
     * 返回当天已分享次数及下一次分享可得青豆值
     */
    @RequestMapping(value = "/post_invite_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result invite(Integer times, Integer qingdou, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        if (times == null || times < 1 || times > 3) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "当前邀请次数必填且在1-3之间");
        }

        if (qingdou == null || qingdou != times * 10) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "青豆奖励必填且应与当前邀请次数对应");
        }

        Integer count = shareService.getTodayInviteTimes(user);
        if (times != count + 1) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "当前邀请次数与记录不符");
        }

        shareService.updateQDPayForInvite(user, qingdou);
        return Result.BuildSuccessResult("邀请成功");
    }

    /**
     * 添加一个字段：每次分享成功之后服务器纪录一下时间
     *
     * @param albumId 图集ID
     */
    @RequestMapping(value = "/post_insert_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result insert(HttpServletRequest request, Integer albumId) throws ParseException {
        User user = (User) request.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        RecordQingdou record = new RecordQingdou();
        record.setUserId(user.getId());
        record.setRecType(Constant.QD_INCOME_SHARE);
        record.setAlbumId(albumId);
        record.setCreateTime(new Date());
        record.setCurrent(1);
        //if (shareService.ifEverShare(user, albumId)) {
        if(shareService.queryRecordCount(record) > 10){
            record.setQingdouAmount(0);
            int a = shareService.insert(record);
            if (a != 1) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "数据异常");
            }
            map.put("flag", false);
            map.put("qigndou", 0);
            return Result.BuildSuccessResult(map);
        }
        record.setQingdouAmount((int)(2+Math.random()*(10-2+1)));//前10次分享获得[2,10]随机青豆
        userService.addQingdou(user.getId(), record.getQingdouAmount());
        int a = shareService.insert(record);
        if (a != 1) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "数据异常");
        }
        map.put("flag", true);
        map.put("qingdou", record.getQingdouAmount());
        return Result.BuildSuccessResult(map);
    }

    /**
     * 是不是首次分享
     */
    @RequestMapping(value = "/post_ifEverShare_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result ifEverShare(HttpServletRequest request, Integer albumId) {
        Map<String, Object> map = new HashMap<>();
        User user = (User) request.getAttribute("user");
        boolean flag = false;
        int qingdou = 0;
        if (user != null) {
            flag = shareService.ifEverShare(user, albumId);
            if (flag) {
                RecordQingdou recordQingdou_ = new RecordQingdou();
                recordQingdou_.setUserId(user.getId());
                recordQingdou_.setRecType(Constant.QD_INCOME_SHARE);
                recordQingdou_.setAlbumId(albumId);
                RecordQingdou recordQingdou = shareService.queryRecordQingdou(recordQingdou_);
                qingdou = recordQingdou.getQingdouAmount();
            }
        }
        map.put("flag", flag);
        map.put("qingdou", qingdou);
        return Result.BuildSuccessResult(map);
    }

    /**
     * 分享
     *
     * @param passport
     * @return shareTimes 已分享次数
     */
    @RequestMapping(value = "/share_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result shareNew(HttpServletRequest request, @RequestParam(value = "passport", defaultValue = "") String passport) {
        User user = (User) request.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        RecordQingdou record = new RecordQingdou();
        record.setUserId(user.getId());
        record.setRecType(Constant.QD_INCOME_SHARE);
        record.setCreateTime(new Date());
        record.setAlbumId(null);
        record.setQingdouAmount( (int)(2+Math.random()*(10-2+1)) );//初次分享获得[1,10]随机青豆
        userService.addQingdou(user.getId(), record.getQingdouAmount());
        int a = shareService.insert(record);
        if (a != 1) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "数据异常");
        }
        RecordQingdou record_ = new RecordQingdou();
        record_.setRecType(Constant.QD_INCOME_SHARE);
        record_.setUserId(user.getId());
        map.put("shareTimes", shareService.queryRecordCount(record_));
        return Result.BuildSuccessResult(map);
    }

    //今日分享次数
    @RequestMapping(value = "/share_today_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getTodayShareCounts(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        RecordQingdou _qingdou = new RecordQingdou();
        _qingdou.setUserId(user.getId());
        _qingdou.setCurrent(1);
        _qingdou.setRecType(Constant.QD_INCOME_SHARE);
        map.put("shareCounts",shareService.queryRecordCount(_qingdou));
        return Result.BuildSuccessResult(map);
    }

}
