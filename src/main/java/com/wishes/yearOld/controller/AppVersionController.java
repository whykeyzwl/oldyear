package com.wishes.yearOld.controller;

import com.wishes.yearOld.model.AppVersion;
import com.wishes.yearOld.model.ResponseCode;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.service.AppVersionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
@Controller
@RequestMapping("/appversion")
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping("/post_version.json")
    @ResponseBody
    public Result getAppVersion(Integer type){
        AppVersion version = appVersionService.getAppVersion(type);
        if(version == null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, (type==2?"IOS":"Android")+"未发布过版本");
        }
        return Result.BuildSuccessResult(version);
    }

}
