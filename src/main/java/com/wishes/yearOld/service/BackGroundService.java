package com.wishes.yearOld.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wishes.yearOld.model.FileUploadStatus;

public interface BackGroundService {

	 
	  
	  
	    /** 
	     * 从文件路径中取出文件名 
	     */  
	  String takeOutFileName(String filePath);  
	  
	    /** 
	     * 从request中取出FileUploadStatus Bean 
	     */  
	   FileUploadStatus getStatusBean(HttpServletRequest request) ;  
	  
	    /** 
	     * 把FileUploadStatus Bean保存到类控制器BeanControler 
	     * @return 
	     */  
	   void saveStatusBean(HttpServletRequest request,FileUploadStatus statusBean);
	  
	    /** 
	     * 删除已经上传的文件 
	     */  
	    void deleteUploadedFile(HttpServletRequest request); 
	  
	    /** 
	     * 上传过程中出错处理 
	     */  
	    void uploadExceptionHandle(HttpServletRequest request,String errMsg) ;											 
	  
	    /** 
	     * 初始化文件上传状态Bean 
	     */  
	  FileUploadStatus initStatusBean(HttpServletRequest  request); 
	  
	    /** 
	     * 处理文件上传 
	     */  
	   void processFileUpload(HttpServletRequest request,HttpServletResponse response);
	  
	    /** 
	     * 回应上传状态查询 
	     */  
	   void responseStatusQuery(HttpServletRequest request,HttpServletResponse response); 
	  
	    /** 
	     * 处理取消文件上传 
	     */  
	    void processCancelFileUpload(HttpServletRequest request,HttpServletResponse response); 
	  
	    void doPost(HttpServletRequest request,HttpServletResponse response);



}
