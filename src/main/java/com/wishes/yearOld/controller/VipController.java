package com.wishes.yearOld.controller;

import com.wishes.yearOld.model.PersonalWebsite;
import com.wishes.yearOld.model.Photoalbuminfo;
import com.wishes.yearOld.model.Piclist;
import com.wishes.yearOld.model.ResponseCode;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.model.Vip;
import com.wishes.yearOld.service.PersonalWebsiteService;
import com.wishes.yearOld.service.PhotoAlbumService;
import com.wishes.yearOld.service.VipService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by IntelliJ IDEA.
 * User: zwl
 * Date: 2017/04/26
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/vip")
public class VipController {

    @Autowired
    private VipService vipService;//个人信息注入
    
    @RequestMapping(value ="/getVipinfo.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getVipinfo(HttpServletRequest request){
    	List<Vip> vipLst = vipService.getAllvip(); 	
    	return Result.BuildSuccessResult(vipLst);//返回最终信息
    }
}
