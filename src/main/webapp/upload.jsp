<%@ page language="java" pageEncoding="UTF-8" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <base href="<%=basePath%>">
  <style>
    #uploader{
      position:relative;
    }
    #uploader_queue{
      position:absolute;
      width:600px;
      left:200px;
      top:0;
    }
  </style>
  <title>My JSP 'upLoad.jsp' starting page</title>
  <link type="text/css" rel="stylesheet" href="uploadify/uploadify.css"/>
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
  <meta http-equiv="description" content="This is my page">
  <script type="text/javascript" src="uploadify/jquery-1.8.3.min.js"></script>
  <script type="text/javascript" src="uploadify/jquery.uploadify.min.js"></script>
  <script>
    $(function() {
      $("#file_upload").uploadify({
        'auto' : false,
        'method' : "post",
        'formData' : {'folder' : 'cache'},
        'height' : 30,
        'swf' : '<%=basePath%>uploadify/uploadify.swf', // flash
        'uploader' : '<%=basePath%>fileUpload.json;jsessionid=${pageContext.session.id}', // 数据处理url
        'width' : 120,
        'fileTypeDesc':'.jpg',
        'fileTypeExts' : '*.gif;*.jpg;*.png;*.jpeg;*.bmp',
        'fileSizeLimit' : '100MB',
        'buttonText' : '选择文件',
        //'uploadLimit' : 5,
        'successTimeout' : 30,
        'requeueErrors' : true,
        'removeTimeout' : 2,
        'removeCompleted' : true,
        'queueSizeLimit' :10,
        'queueID'  : 'uploader_queue',
        'progressData' : 'speed',
        //'checkExisting':true,
        'onInit' : function (){
        },
        // 单个文件上传成功时的处理函数//主要做预览
        'onUploadSuccess' : function(file, data, response){
          $("#uploader_view").append('<img height="60" alt="" src="'+ data + '"/>');
        },
        'onQueueComplete' : function(queueData) {
          $('#uploader_msg').html(queueData.uploadsSuccessful + ' files were successfully uploaded.');
        }
      });
    });
  </script>
</head>
<body>
<a href="<%=basePath%>upload">This is my JSP page. </a><br>
<div id="uploader">
  <form><input type="file" name="file" id="file_upload" /></form>
  <a href="javascript:$('#file_upload').uploadify('upload','*')">上传</a>&nbsp;
  <a href="javascript:$('#file_upload').uploadify('stop')">取消上传</a>
  <div id="uploader_queue"></div>
  <div id="uploader_msg"></div>
  <div id="uploader_view"></div>
</div>
</body>
</html>