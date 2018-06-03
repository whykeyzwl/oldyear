package com.wishes.yearOld.controller;

import com.alipay.api.internal.util.StringUtils;
import com.wishes.yearOld.common.PathUtil;
import com.wishes.yearOld.model.ResponseCode;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.service.IUserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-21
 * Time: 下午5:48
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/common")
public class CommonController {
	
    @Autowired
	IUserService userService;
    
    private static final Logger logger = Logger.getLogger(CommonController.class);

    @RequestMapping(value = "/post_uploadimg_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result uploadImage(String passportId,@RequestParam("file") CommonsMultipartFile file) {
        if(!file.isEmpty()) {
        	  int curUserId = 0;
        	  if(StringUtils.isEmpty(passportId)){
                  // 从缓存读个人信息
                  User user = userService.loadUserFromCache(passportId);
                  if(user!=null) curUserId = user.getId();
              }
        	  
            String imageName = file.getOriginalFilename();
            String lastName = imageName.substring(imageName.lastIndexOf("."));
            //String reg = "png|gif|jpg|jpeg|bmp|rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov|wmv|rar|zip|doc|xls|chm|hlp|swf|html|htm|js|torrent|pdf|shtml|css";
            String reg = "png|gif|jpg|jpeg|bmp";
            Pattern pattern = Pattern.compile("[.]{1}(" + reg + ")", Pattern.CASE_INSENSITIVE + Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(lastName);
            if(!matcher.find()){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "请上传图像文件!");
            }
            FileOutputStream os = null;
            InputStream in = null;
            try {
                File imageFile = PathUtil.getRandImageFile(lastName);
               
                os = new FileOutputStream(imageFile);
                in = file.getInputStream();
                int b;
                while ((b = in.read()) != -1) {
                    os.write(b);
                }
                os.flush();
                if(String.valueOf(curUserId)!=""){
              	   User users = new User();
              	   users.setId(curUserId);//用户id
              	   users.setFace("http://47.95.207.69/"+PathUtil.getImageFileUrlFromPath(imageFile.getPath()));//用户封面
              	   userService.updateCover(users);
                 }
                logger.info("上传图像文件:" + imageFile.getPath()+"最终图片路径："+PathUtil.getImageFileUrlFromPath(imageFile.getPath()));
                //TODO 待修改返回文件地址
                return Result.BuildSuccessResult(PathUtil.getImageFileUrlFromPath(imageFile.getPath()));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(),e);
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"保存文件路径错误");
            } catch (IOException e1) {
                logger.error(e1.getMessage(),e1);
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"IO异常");
            }finally {
                try {
                    if(os != null)
                        os.close();
                    if(in != null)
                        in.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"IO异常");
                }
            }
        }
        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "文件为空");
    }
    /**
     * User: zwl
     * Date: 2017/05/02
     * Time: 10:17
     * To change this template use File | Settings | File Templates.
     * 上传主页封面图片
     */
    @RequestMapping(value = "/uploadhomeimage.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result uploadhomeimage(@RequestParam("file") CommonsMultipartFile file,@RequestParam(value = "userid", required = false)int userid,HttpServletRequest req) {
    	 //更改用户封面的地址
        
        if(!file.isEmpty()) {
            String imageName = file.getOriginalFilename();
            String lastName = imageName.substring(imageName.lastIndexOf("."));
            //String reg = "png|gif|jpg|jpeg|bmp|rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov|wmv|rar|zip|doc|xls|chm|hlp|swf|html|htm|js|torrent|pdf|shtml|css";
            String reg = "png|gif|jpg|jpeg|bmp";
            Pattern pattern = Pattern.compile("[.]{1}(" + reg + ")", Pattern.CASE_INSENSITIVE + Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(lastName);
            if(!matcher.find()){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "请上传图像文件!");
            }
          
            FileOutputStream os = null;
            InputStream in = null;
            try {
                File imageFile = PathUtil.getRandImageFile(lastName);
                if(String.valueOf(userid)!=""){
             	   User users = new User();
             	   users.setId(userid);//用户id
             	   users.setTimelineCover("/"+PathUtil.getImageFileUrlFromPath(imageFile.getPath()));//用户封面
             	   userService.updateCover(users);
                }
                os = new FileOutputStream(imageFile);
                in = file.getInputStream();
                int b;
                while ((b = in.read()) != -1) {
                    os.write(b);
                }
                os.flush();
                logger.info("上传图像文件:" + PathUtil.getImageFileUrlFromPath(imageFile.getPath()));
                
             
                // 待修改返回文件地址
                return Result.BuildSuccessResult(PathUtil.getImageFileUrlFromPath(imageFile.getPath()));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(),e);
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"保存文件路径错误");
            } catch (IOException e1) {
                logger.error(e1.getMessage(),e1);
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"IO异常");
            }finally {
                try {
                    if(os != null)
                        os.close();
                    if(in != null)
                        in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"IO异常");
                }
            }
        }
        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "文件为空");
    }
    
    @RequestMapping(value = "/post_uploadvideo_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result uploadVideo(@RequestParam("file") CommonsMultipartFile file) {
        if(!file.isEmpty()) {
            String videoName = file.getOriginalFilename();
            String lastName = videoName.substring(videoName.lastIndexOf("."));
            //String reg = "png|gif|jpg|jpeg|bmp|rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov|wmv|rar|zip|doc|xls|chm|hlp|swf|html|htm|js|torrent|pdf|shtml|css";
            String reg = "mp4|avi|wmv|bmp|rm|rmvb|mpeg1-4|3gp|dmv|amv|mov|mtv";
            Pattern pattern = Pattern.compile("[.]{1}(" + reg + ")", Pattern.CASE_INSENSITIVE + Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(lastName);
            if(!matcher.find()){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "请上传视频文件!");
            }
            FileOutputStream os = null;
            InputStream in = null;
            try {
                File videoFile = PathUtil.getRandVideoFile(lastName);
                os = new FileOutputStream(videoFile);
                in = file.getInputStream();
                int b;
                while ((b = in.read()) != -1) {
                    os.write(b);
                }
                os.flush();
                logger.info("上传视频文件:" + videoFile.getPath());
                //TODO 待修改返回文件地址
                return Result.BuildSuccessResult(PathUtil.getVideoFileUrlFromPath(videoFile.getPath()));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(),e);
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"保存文件路径错误");
            } catch (IOException e1) {
                logger.error(e1.getMessage(),e1);
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"IO异常");
            }finally {
                try {
                    if(os != null)
                        os.close(); 
                    if(in != null)
                        in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"IO异常");
                }
            }
        }
        return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "文件为空");
    }
}
