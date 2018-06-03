package com.wishes.yearOld.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;



import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.DateUtils;
import com.wishes.yearOld.common.PathUtil;
import com.wishes.yearOld.common.PicExifUtil;
import com.wishes.yearOld.common.StringSort;
import com.wishes.yearOld.model.GodViewLikeVO;
import com.wishes.yearOld.model.PhotoAlbum;
import com.wishes.yearOld.model.ResponseCode;
import com.wishes.yearOld.model.Result;
import com.wishes.yearOld.model.TgodPhoto;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.service.IUserService;
import com.wishes.yearOld.service.PhotoAlbumService;
import com.wishes.yearOld.service.TgodPhotoService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magick.MagickException;
/**
 * Created by tmg-zwl on 2017/5/2.
 */
@Controller
@RequestMapping("/uploadphotoalbum")
public class UploadPhotoAlbumController {

  private static final Logger logger = Logger.getLogger(UploadPhotoAlbumController.class);
    
 //  private static String ROOTDIR = Configurations.getFileRepository();

    @Autowired
    PhotoAlbumService photoAlbumService;//图集表处理
    
    @Autowired
    TgodPhotoService tgodPhotoService;//图片处理

    @Autowired
    IUserService userService;
    
    

    /**
     * 用户上传自定义图集接口，则返回空.
     * @param passportId 非必填，如果已登陆需要传入此参数
     * @return
     * @author zwl
     * @throws IOException 
     * @throws IllegalStateException 
     */
    //int flag = 0;//默认横图
    @RequestMapping(value = "/post_uploadphotoalbumbak.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result photoinfo(@RequestParam(value="files",required=false) CommonsMultipartFile[] files,HttpServletRequest request,@RequestParam(value = "userid", required = false)Integer userid,HttpServletResponse response) throws IllegalStateException, IOException, MagickException {
    	logger.info("文件名称:====="+files);
    	
        PhotoAlbum photoAlbums = new PhotoAlbum();//图集信息
        String contents = request.getParameter("content");//文字内容描述
        String titles = request.getParameter("title");//标题
        String positions = request.getParameter("position");//所在位置
        String username="";
        if(request.getParameter("username")!="" && request.getParameter("username")!="null"){        
        username = request.getParameter("username");//用户名称
        }
        photoAlbums.setModelId(userid);
        photoAlbums.setModelName(username);
        photoAlbums.setDescription(contents);
        photoAlbums.setTitle(titles);
        photoAlbums.setCreatedTime(new Date());//创建时间
        photoAlbums.setPublishTime(new Date());//
        photoAlbums.setLastUpdateTime(new Date());
        photoAlbums.setPositioninfo(positions);
        photoAlbums.setIsQdk(0);//用户自定义发布
        List<TgodPhoto> listImage=new ArrayList<TgodPhoto>();//存放图片信息
        int pagenums = 0;//存放图片的顺序
        if(files!=null && files.length>0){  
       // CommonsMultipartFile[] filesy =	StringSort.getUrlParam(files);
        for (CommonsMultipartFile mf : files) {  
            if(!mf.isEmpty()){  
            	   String imageName = mf.getOriginalFilename();
            	   logger.info("文件名称:" + imageName);
                   String lastName = imageName.substring(imageName.lastIndexOf("."));
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
                       TgodPhoto tgodPhoto = new TgodPhoto();
                       pagenums++;//每次循环顺序加1
                        //获取图片exif信息
                       String exif = PicExifUtil.getPicInfo(imageFile.getPath());
                       tgodPhoto.setPath("/"+PathUtil.getImageFileUrlFromPath(imageFile.getPath()));//图片路径
                       tgodPhoto.setBigImage("/"+PathUtil.getImageFileUrlFromPath(imageFile.getPath()));//大图路径
                       tgodPhoto.setExif(exif);
                       tgodPhoto.setPage(pagenums);
                       listImage.add(tgodPhoto);// 图片路径按顺序添加list中
                       
                       os = new FileOutputStream(imageFile);
                       in = mf.getInputStream();
                       System.out.println("===="+mf.getSize()+"=="+mf.getName());
                       int length = 0;
                       byte[] buf = new byte[1024];
                       // in.read(buf) 每次读到的数据存放在buf 数组中
                       while ((length = in.read(buf)) != -1) {
                           //在buf数组中取出数据写到（输出流）磁盘上
                           os.write(buf, 0, length);
                       }
                       in.close();
                       os.close();
                       /*int b;
                       while ((b = in.read()) != -1) {
                           os.write(b);
                       }
                       os.flush();*/
                       logger.info("上传图像文件:" + imageFile.getPath()+"最终图片路径："+PathUtil.getImageFileUrlFromPath(imageFile.getPath()));

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
          }
        }
        
        photoAlbums.setPages(listImage.size());
        photoAlbumService.insertSelective(photoAlbums);//保存图集信息
        if(listImage!=null && listImage.size()>0){
        int ids =	photoAlbums.getId();
        for (TgodPhoto photos : listImage) { 
        	photos.setAlbumid(ids);
        	tgodPhotoService.insertSelective(photos);//保存图片信息
          }
        }
        return Result.BuildSuccessResult(photoAlbums);
            
    }
    /**
     * 用户上传自定义图集接口，则返回空.
     * @param passportId 非必填，如果已登陆需要传入此参数
     * @return
     * @author zwl
     * @throws IOException 
     * @throws IllegalStateException 
     */
    //int flag = 0;//默认横图
    @RequestMapping(value = "/post_uploadphotoalbum.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result photoinfo(HttpServletRequest request,@RequestParam(value = "userid", required = false)Integer userid,HttpServletResponse response) throws IllegalStateException, IOException, MagickException {
        PhotoAlbum photoAlbums = new PhotoAlbum();//图集信息
        String contents = request.getParameter("content");//文字内容描述
        String titles = request.getParameter("title");//标题
        String positions = request.getParameter("position");//所在位置
        String username="";
        if(request.getParameter("username")!="" && request.getParameter("username")!="null"){        
        username = request.getParameter("username");//用户名称
        }
        photoAlbums.setModelId(userid);
        photoAlbums.setModelName(username);
    /*    User usr = userService.loadUserFromCache(userid.toString());
        photoAlbums.setModelId(usr.getId());
        photoAlbums.setModelName(usr.getNickName());*/
        photoAlbums.setDescription(contents);
        photoAlbums.setTitle(titles);
        photoAlbums.setCreatedTime(new Date());//创建时间
        photoAlbums.setPublishTime(new Date());//
        photoAlbums.setLastUpdateTime(new Date());
        photoAlbums.setPositioninfo(positions);
        photoAlbums.setIsQdk(0);//用户自定义发布
        System.out.println("======="+request.getParameter("photo"));
        String listImage=request.getParameter("photo");
        System.out.println("======="+listImage);
        JSONArray json=JSON.parseArray(listImage);
        photoAlbums.setPages(json.size());
        photoAlbumService.insertSelective(photoAlbums);//保存图集信息
        int ids =	photoAlbums.getId();
        int pagenums = 0;//存放图片的顺序
        JSONObject jsonOne;  
        TgodPhoto photos = new TgodPhoto();
        if(json!=null && json.size()>0){
        for(int i=0;i<json.size();i++){  
                 jsonOne = json.getJSONObject(i);  
                 photos.setAlbumid(ids);
                 photos.setPage(Integer.parseInt(jsonOne.get("page").toString()));
                 photos.setPath(jsonOne.get("path").toString().replace("\"", ""));
              	tgodPhotoService.insertSelective(photos);//保存图片信息
         }  
       
        }
        return Result.BuildSuccessResult(photoAlbums);
            
    }  
    /**
     * 用户上传自定义图集接口，则返回空.（小程序）
     * @param passportId 非必填，如果已登陆需要传入此参数
     * @return
     * @author zwl
     * @throws IOException 
     * @throws IllegalStateException 
     * @throws FileUploadException 
     */
   
    @RequestMapping(value = "/post_uploadphotoalbumxcx.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object photoinfoXcx(HttpServletRequest request,@RequestParam(value = "userid", required = false)Integer userid,HttpServletResponse response) throws IllegalStateException, IOException, MagickException, FileUploadException {
    	
    	 Map<String,MultipartFile> map = new HashMap();
         if(request.getParameter("folder")==null||request.getParameter("folder")==""){
             logger.info("-------------------request.getParameter('folder')::::" + request.getParameter("folder") + " then return");
         }

         response.setContentType("text/html");
         response.setCharacterEncoding("UTF-8");
         String path = request.getSession().getServletContext().getRealPath("/");
         String fileD = request.getParameter("folder");
         String sourcePath = path + "upload/source/";
         path = path + "upload/" + fileD + "/";
         logger.info("-------------------UpLoadify-path::::" + path);
         File folder = new File(path);
         File sourceFolder = new File(sourcePath);
         if (!folder.exists()) {
             //文件夹不存在则创建
             logger.info("-------------------UpLoadify::::" + "创建文件夹" + folder);
             folder.mkdirs();
         }

         if (!sourceFolder.exists()) {
             //文件夹不存在则创建
             logger.info("-------------------UpLoadify::::" + "创建文件夹source");
             sourceFolder.mkdirs();
         }
         // 创建缓冲区
         DiskFileItemFactory factory = new DiskFileItemFactory();
         // 设置缓冲区大小 1*1024*1024 设置1Mb
         factory.setSizeThreshold(5 * 1024 * 1024);
         // 设置缓冲临时目录
         factory.setRepository(new File(path));
         ServletFileUpload upload = new ServletFileUpload(factory);
         // 设置编码
         upload.setHeaderEncoding("UTF-8");
         response.setContentType("text/html;charset=utf-8");
         // 设置文件最大值,这里设置20Mb,20*1024*1024;
         upload.setSizeMax(20 * 1024 * 1024);
         map = ((MultipartHttpServletRequest)request).getFileMap();
         String result = "";
         for(Map.Entry<String,MultipartFile> entity:map.entrySet()){
             MultipartFile mf = entity.getValue();
             
             //File file = new File(sourcePath+mf.getOriginalFilename());
             String imageName = mf.getOriginalFilename();
             String lastName = imageName.substring(imageName.lastIndexOf("."));
             File imageFile = PathUtil.getRandImageFile(lastName);
             File imageFileSource = PathUtil.getRandImageFilePath(lastName,sourcePath);
             System.out.println("imageFileSource================"+imageFileSource);
             PathUtil.getImageFileUrlFromPath(imageFile.getPath());
             FileCopyUtils.copy(mf.getBytes(),imageFile);
             FileCopyUtils.copy(mf.getBytes(),imageFileSource);
             System.out.println("imageFileSource.getPath()================"+imageFileSource.getPath());
             result =imageFileSource.getPath().replace("/home/yearold/yearOld", "");
         }
         return result;
          
    }
    @RequestMapping(value = "/picture", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void uploadPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取文件需要上传到的路径
        String path = request.getRealPath("/upload") + "/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        logger.debug("path=" + path);

        request.setCharacterEncoding("utf-8");  //设置编码
        //获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();

        //如果没以下两行设置的话,上传大的文件会占用很多内存，
        //设置暂时存放的存储室,这个存储室可以和最终存储文件的目录不同
        /**
         * 原理: 它是先存到暂时存储室，然后再真正写到对应目录的硬盘上，
         * 按理来说当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的
         * 然后再将其真正写到对应目录的硬盘上
         */
        factory.setRepository(dir);
        //设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
        factory.setSizeThreshold(1024 * 1024);
        //高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = upload.parseRequest(request);
            FileItem picture = null;
            for (FileItem item : list) {
                //获取表单的属性名字
                String name = item.getFieldName();
                //如果获取的表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    //获取用户具体输入的字符串
                    String value = item.getString();
                    request.setAttribute(name, value);
                    logger.debug("name=" + name + ",value=" + value);
                } else {
                    picture = item;
                }
            }

            //自定义上传图片的名字为userId.jpg
            String fileName = request.getAttribute("userId") + ".jpg";
            String destPath = path + fileName;
            logger.debug("destPath=" + destPath);

            //真正写到磁盘上
            File file = new File(destPath);
            OutputStream out = new FileOutputStream(file);
            InputStream in = picture.getInputStream();
            int length = 0;
            byte[] buf = new byte[1024];
            // in.read(buf) 每次读到的数据存放在buf 数组中
            while ((length = in.read(buf)) != -1) {
                //在buf数组中取出数据写到（输出流）磁盘上
                out.write(buf, 0, length);
            }
            in.close();
            out.close();
        } catch (FileUploadException e1) {
            logger.error("", e1);
        } catch (Exception e) {
            logger.error("", e);
        }


        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        printWriter.write(JSON.toJSONString(res));
        printWriter.flush();
    }
    /**
     *  * 修改用户价格
     * @return
     * @author zwl
     * @param id 图集id，viewprice浏览价格
     */
    @RequestMapping(value="/updateViewPrice.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public int updatePhotoAlbumPrice(PhotoAlbum record,HttpServletRequest request){
       return photoAlbumService.updateByPrimaryKeySelective(record);
    } 
    /**
     *  * 用户点赞情况
     * @return
     * @author zwl
     * 用户图集点赞/取消点赞
     * @param albumId 图集id
     */
    @RequestMapping(value="/albumlike.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result viewLike(Integer albumId,HttpServletRequest request){
        if(albumId==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"图集Id不能为空");
        }
        Integer likeCount = photoAlbumService.selectAlbumLikeCount(albumId);
        if(likeCount==null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"图集不存在");
        }

        GodViewLikeVO likeVO =  new GodViewLikeVO();
        String userIds = request.getParameter("userid");
        likeVO.setUserId(Integer.parseInt(userIds));
        likeVO.setLikeId(albumId);
        likeVO.setLikeType(Constant.LIKE_TYPE_ALBUM);
        //查询是否已赞
        GodViewLikeVO vo = photoAlbumService.selectIsLikeAlbum(likeVO);
        try{
            if(vo != null){
                //取消点赞
                photoAlbumService.giveDownAlbum(vo);
                likeCount--;
                return Result.BuildSuccessResult(likeCount);
            }else{
                //点赞
                likeVO.setCreateTime(new Date());
                photoAlbumService.giveUpAlbum(likeVO);
                likeCount++;
                return Result.BuildSuccessResult(likeCount);
            }
        }catch(Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    } 
}
