package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.model.PersonalWebsite;
import com.wishes.yearOld.model.PhotoAlbum;
import com.wishes.yearOld.model.Photoalbuminfo;
import com.wishes.yearOld.model.Piclist;
import com.wishes.yearOld.model.ResponseCode;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.service.IUserService;
import com.wishes.yearOld.service.PersonalWebsiteService;
import com.wishes.yearOld.service.PhotoAlbumService;

import java.math.BigDecimal;
import java.util.ArrayList;

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
@RequestMapping("/personalwebsite")
public class PersonalWebsiteController {

    @Autowired
    private PersonalWebsiteService personalWebsiteService;//个人信息注入
    
    @Autowired
    PhotoAlbumService photoAlbumService;//查询图集以及图片信息
    
    @Autowired
    IUserService userService;
    
    @RequestMapping(value ="/post_personalinfo.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getPersonalWebsite(HttpServletRequest request,PersonalWebsite personalWebsite,Photoalbuminfo photoalbuminfo){
    	int starts = personalWebsite.getStart();
    	int pageSizes = personalWebsite.getPageSize();
    	
    	Photoalbuminfo photoalbuminfosy = new Photoalbuminfo();
        ArrayList<Photoalbuminfo>  photoalbuminfolst =	photoAlbumService.queryBuminfo(photoalbuminfosy,starts*pageSizes,pageSizes);//图集信息
         /*
          * 循环图集信息
          * */
        for(Photoalbuminfo photoalbuminfos : photoalbuminfolst){
        	/**
        	 *获取图集id根据id查询对应的图片信息
        	 */
        	if(photoalbuminfos.getPositioninfo()==""){
        	photoalbuminfos.setPositioninfo(" ");
        	}
        	
        	if(photoalbuminfos.getViewprice()==null){
            	photoalbuminfos.setViewprice(new BigDecimal(0.00));
            	}
        	personalWebsite.setUserid(String.valueOf(photoalbuminfos.getModelId()));
        	PersonalWebsite personalWebsites = personalWebsiteService.getPersonalWebsite(personalWebsite);
        	if(personalWebsites!=null){
        		photoalbuminfos.setModelFace(personalWebsites.getUserHerderPic());	
        	}
        	ArrayList<Piclist>  Piclists = photoAlbumService.queryPiclist(photoalbuminfos.getPhotoalbumId());//图片信息
        	/**
        	 *获取的图片信息放入图集中
        	 */
        	photoalbuminfos.setPiclist(Piclists);
        }
     
        
        return Result.BuildSuccessResult(photoalbuminfolst);//返回最终信息
        }
    
    @RequestMapping(value ="/post_albuminfo.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAlbuminfo(HttpServletRequest request,String passportId ,PersonalWebsite personalWebsite,Photoalbuminfo photoalbuminfo){
    	int starts = personalWebsite.getStart();
    	int pageSizes = personalWebsite.getPageSize();
    	   User user = null;
           if(StringUtils.isNotEmpty(passportId)){
               // 从缓存读个人信息
              user = userService.loadUserFromCache(passportId);
           }
        ArrayList<Photoalbuminfo>  photoalbuminfolst =	photoAlbumService.queryBuminfo(photoalbuminfo,0,1);//图集信息
        PhotoAlbum a = photoAlbumService.detail(photoalbuminfo.getPhotoalbumId(), user, false);
        int permitTypes = a.getPermission().getPermitType();
         /*
          * 循环图集信息
          * */
        for(Photoalbuminfo photoalbuminfos : photoalbuminfolst){
        	/**
        	 *获取图集id根据id查询对应的图片信息
        	 */
        	if(photoalbuminfos.getPositioninfo()==""){
        	photoalbuminfos.setPositioninfo(" ");
        	}
        	photoalbuminfos.setPermitType(permitTypes);
        	personalWebsite.setUserid(String.valueOf(photoalbuminfos.getModelId()));
        	PersonalWebsite personalWebsites = personalWebsiteService.getPersonalWebsite(personalWebsite);
        	if(personalWebsites!=null){
        		photoalbuminfos.setModelFace(personalWebsites.getUserHerderPic());	
        	}
        	ArrayList<Piclist>  Piclists = photoAlbumService.queryPiclistAll(photoalbuminfos.getPhotoalbumId());//图片信息
        	/**
        	 *获取的图片信息放入图集中
        	 */
        	photoalbuminfos.setPiclist(Piclists);
        }
       /**
    	 *用户信息中放入图集信息
    	 */
        return Result.BuildSuccessResult(photoalbuminfolst);//返回最终信息
        }
}
