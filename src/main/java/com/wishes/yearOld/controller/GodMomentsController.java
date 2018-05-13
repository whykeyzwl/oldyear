package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.model.GodMoments;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.service.IGodMomentsService;
import com.wishes.yearOld.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20 0020.
 */
@Controller
@RequestMapping("/goddess")
public class GodMomentsController {

    @Autowired
    private IGodMomentsService godMomentsService;

    @Autowired
    IUserService userService;

    @RequestMapping(value="/query.json",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result queryGodNess(HttpServletRequest req,Integer pageNo,Integer typeLevel, Integer pageSize){
        String passportId = req.getParameter("passportId");
        GodMoments moments = new GodMoments();
        moments.setPageNo(pageNo);
        moments.setPageSize(pageSize);
        User user = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            user = userService.loadUserFromCache(passportId);
            moments.setUserID(user == null ? 0:user.getId());
        }
        int controlLevels =0;
        if(typeLevel==null){
        	controlLevels = Constant.STREAM_CONTROL_LEVELANDR;//安卓获取级别配置文件
        }else if(typeLevel==1){
        	controlLevels = Constant.STREAM_CONTROL_LEVELIOS;//苹果获取级别配置文件
        }else if(typeLevel==2){
        	controlLevels = Constant.STREAM_CONTROL_LEVELANDR;//安卓获取级别配置文件
        }else if(typeLevel==3){
        	controlLevels = Constant.STREAM_CONTROL_LEVELWX;//微信获取级别配置文件
        }
        moments.setControlLevel(controlLevels);
        List<GodMoments> list = godMomentsService.queryGodNess(moments);
        return Result.BuildSuccessResult(list);
    }

}
