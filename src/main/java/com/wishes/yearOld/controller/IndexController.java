package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.BannerService;
import com.wishes.yearOld.service.IUserService;
import com.wishes.yearOld.service.PhotoAlbumService;
import com.wishes.yearOld.utils.CrossUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2016/9/27
 * Description:
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private PhotoAlbumService photoAlbumService;
    @Autowired
    BannerService bannerService;
    @Autowired
    IUserService userService;

    /**
     * 获取首页焦点图
     */
    @RequestMapping(value = "/getFocus.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getFocus() {
        List<Banner> list = bannerService.queryBanners(new Banner());
        if (list == null) {
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "无有效焦点图");
        }
        return Result.BuildSuccessResult(list);
    }

    /**
     * 获取热门词
     */
    @RequestMapping(value = "/getKeyword.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getKeyword() {
        List<String> list = bannerService.queryKeywords();
        if (list == null) {
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "无热门词");
        }
        return Result.BuildSuccessResult(list);
    }

    /**
     * 返回首页列表
     *
     * @param pageNo     页号
     * @param pageSize   页面大小
     * @param passportId
     * @returns {*[]}
     * 1推荐、2限免
     */

    @RequestMapping(value = "/get_home_album.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result GetHome(HttpServletResponse resp,String passportId, @RequestParam(value = "pageNo", required = false) Integer pageNo,@RequestParam(value = "typeLevel", required = false) Integer typeLevel, @RequestParam(value = "pageSize", required = false) Integer pageSize, PhotoAlbum photoAlbum) {
        photoAlbum.setStart(pageNo);
        photoAlbum.setPageSize(pageSize);
        photoAlbum.setSortField("publishTime");
        User _user = null;
        if (StringUtils.isNotEmpty(passportId)) {
            _user = userService.loadUserFromCache(passportId);
        }
        if(_user != null){
            photoAlbum.setUserId(_user.getId());
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
        photoAlbum.setControlLevel(controlLevels);
        LinkedList<PhotoAlbum> photoAlbumList = new LinkedList<PhotoAlbum>();
        photoAlbumList = photoAlbumService.getHomeAlbums(photoAlbum);
        PhotoAlbum _photo = null;
        if(pageNo <= 1 || pageNo == null){
            _photo = photoAlbumService.getHomeAlbumsRand(photoAlbum);
        }
        if(_photo != null){
        	//小程序默认不随机选择推荐数据
        /*	if(typeLevel==null){
            photoAlbumList.addFirst(_photo);
        	}else if(typeLevel==1){
        	photoAlbumList.addFirst(_photo);	
            }else if(typeLevel==2){
            photoAlbumList.addFirst(_photo);	
            }*/
        }
        if (photoAlbumList.size() == 0) {
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "你的访问记录不存在");
        }
        for (PhotoAlbum album : photoAlbumList) {
            album.setModelFace(userService.detail(album.getModelId(), null).getFaceUrl());
        }
        if (StringUtils.isNotEmpty(passportId)) {
            User user = userService.loadUserFromCache(passportId);
            if (user != null) {
                for (PhotoAlbum album : photoAlbumList) {
                	
                    album.setCollection(userService.selectIsCollection(album.getId(), 2, user.getId()));
                }
            }
        }
        CrossUtil.SetHttpServletResponse(resp);
        return Result.BuildSuccessResult(photoAlbumList);
    }
    
    /**
     * 返回首页最热列表
     *@date 2017/5/19
     * @param pageNo     页号
     * @param pageSize   页面大小
     * @param passportId
     * @returns {*[]}
     * 1推荐、2限免
     */

    @RequestMapping(value = "/get_home_albumHot.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result GetHomeHot(HttpServletResponse resp,String passportId, @RequestParam(value = "pageNo", required = false) Integer pageNo,@RequestParam(value = "typeLevel", required = false) Integer typeLevel, @RequestParam(value = "pageSize", required = false) Integer pageSize, PhotoAlbum photoAlbum) {
        photoAlbum.setStart(pageNo);
        photoAlbum.setPageSize(pageSize);
        photoAlbum.setSortField("view_count");
        User _user = null;
        if (StringUtils.isNotEmpty(passportId)) {
            _user = userService.loadUserFromCache(passportId);
        }
        if(_user != null){
            photoAlbum.setUserId(_user.getId());
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
        photoAlbum.setControlLevel(controlLevels);
        LinkedList<PhotoAlbum> photoAlbumList = new LinkedList<PhotoAlbum>();
        photoAlbumList = photoAlbumService.getHomeAlbumsHot(photoAlbum);
        PhotoAlbum _photo = null;
        if(pageNo <= 1 || pageNo == null){
            _photo = photoAlbumService.getHomeAlbumsRand(photoAlbum);
        }
        if(_photo != null){
        	//小程序默认不随机选择推荐数据
        /*	if(typeLevel==null){
            photoAlbumList.addFirst(_photo);
        	}else if(typeLevel==1){
        	photoAlbumList.addFirst(_photo);	
            }else if(typeLevel==2){
            photoAlbumList.addFirst(_photo);	
            }*/
        }
        if (photoAlbumList.size() == 0) {
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "你的访问记录不存在");
        }
        for (PhotoAlbum album : photoAlbumList) {
            album.setModelFace(userService.detail(album.getModelId(), null).getFaceUrl());
        }
        if (StringUtils.isNotEmpty(passportId)) {
            User user = userService.loadUserFromCache(passportId);
            if (user != null) {
                for (PhotoAlbum album : photoAlbumList) {
                    album.setCollection(userService.selectIsCollection(album.getId(), 2, user.getId()));
                }
            }
        }
        CrossUtil.SetHttpServletResponse(resp);
        return Result.BuildSuccessResult(photoAlbumList);
    }
}
