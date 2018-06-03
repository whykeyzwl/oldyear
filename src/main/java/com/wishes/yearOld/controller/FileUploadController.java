package com.wishes.yearOld.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Created by zouyu on 2016/9/22.
 */
@Controller
public class FileUploadController {

    //日志实例
    private static final Logger logger = Logger.getLogger(FileUploadController.class);

    @RequestMapping(value = "/fileUpload.json", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException {
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
            File file = new File(sourcePath+mf.getOriginalFilename());
            FileCopyUtils.copy(mf.getBytes(),file);
            result =sourcePath+mf.getOriginalFilename();
        }
        return result;
    }
}

