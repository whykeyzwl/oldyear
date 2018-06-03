<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String tag = (String)request.getParameter("tag");
    if("1".equals(tag)){
        basePath = "http://appapi.qingdouke.com/";
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>Uikit Test</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css"	href="css/uikit.gradient.min.css">
    <link rel="stylesheet" type="text/css" href="css/notify.gradient.min.css">
</head>
<body>
<div
        style="width:800px;margin-top:10px;margin-left: auto;margin-right: auto;text-align: center;">
    <h2>Uikit Test</h2>
</div>
<div style="width:800px;margin-left: auto;margin-right: auto;">
    <fieldset class="uk-form">
        <legend>Uikit表单渲染测试</legend>
        <div class="uk-form-row">
            url :<input type="text" id="url" class="uk-width-1-1">
        </div>
        <div class="uk-form-row">
            params :<input type="text" value="id: 1,name:'孙伟',sex:'男',age:25" id="params" class="uk-width-1-1 uk-form-success">
        </div>
        <%--<div class="uk-form-row">--%>
        <%--<input type="text" class="uk-width-1-1 uk-form-danger">--%>
        <%--</div>--%>
        <%--<div class="uk-form-row">--%>
        <%--<input type="text" class="uk-width-1-1">--%>
        <%--</div>--%>
        <%--<div class="uk-form-row">--%>
        <%--<select id="form-s-s">--%>
        <%--<option>---请选择---</option>--%>
        <%--<option>是</option>--%>
        <%--<option>否</option>--%>
        <%--</select>--%>
        <%--</div>--%>
        <%--<div class="uk-form-row">--%>
        <%--<input type="date" id="form-h-id" />--%>
        <%--</div>--%>
    </fieldset>
    <fieldset class="uk-form">
        <legend>基于Restful架构风格的资源请求测试</legend>
        <button class="uk-button uk-button-primary uk-button-large" id="btnGet">GET</button>
        <button class="uk-button uk-button-primary uk-button-large" id="btnAdd">POST</button>
        <button class="uk-button uk-button-primary uk-button-large" id="btnUpdate">PUT</button>
        <button class="uk-button uk-button-danger uk-button-large" id="btnDel">DELETE</button>
        <button class="uk-button uk-button-primary uk-button-large" id="btnList">PATCH</button>
    </fieldset>
    <h>上传</h>
    <form name="userForm" action="common/post_uploadimg_auth.json?passportId=0fc29f28ac6e1d279cc4417daec76baf1475148969478" method="post" enctype="multipart/form-data">
        选择文件：<input type="file" name="file">
        <input type="submit" value="提交">
        <input type="text" name="name">
    </form>
</div>

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/uikit.min.js"></script>
<script type="text/javascript" src="js/notify.min.js"></script>
<script type="text/javascript">
//window.location.href = "wap/qdk-shouye.html";
    (function(window,$){

        var dekota={

            url:'',

            init:function(){
                dekota.url='<%=basePath%>';
                $.UIkit.notify("页面初始化完成", {status:'info',timeout:500});
                $("#btnGet").click(dekota.getPerson);
                $("#btnAdd").click(dekota.addPerson);
                $("#btnDel").click(dekota.delPerson);
                $("#btnUpdate").click(dekota.updatePerson);
                $("#btnList").click(dekota.listPerson);
            },
            getPerson:function(){
                $.ajax({
                    url: dekota.url + $("#url").val(),
                    type: 'GET',
                    dataType: 'json'
                }).done(function(data, status, xhr) {
                            $.UIkit.notify("成功", {status:'success',timeout:1000});
                            $.UIkit.notify(JSON.stringify(data), {status:'success',timeout:5000});
                        }).fail(function(xhr, status, error) {
                            $.UIkit.notify("请求失败！", {status:'danger',timeout:2000});
                        });
            },
            addPerson:function(){
                var json = eval('({' + $("#params").val() + '})');
                $.ajax({
                    url: dekota.url + $("#url").val(),
                    type: 'POST',
                    dataType: 'json',
                    data: json
                    //data: $("#params").val()
                }).done(function(data, status, xhr) {
                            $.UIkit.notify(JSON.stringify(data), {status:'success',timeout:60000});
                        }).fail(function(xhr, status, error) {
                            $.UIkit.notify("请求失败！", {status:'danger',timeout:2000});
                        });
            },
            delPerson:function(){
                $.ajax({
                    url: dekota.url + $("#url").val(),
                    type: 'DELETE',
                    dataType: 'json'
                }).done(function(data, status, xhr) {
                            $.UIkit.notify(JSON.stringify(data), {status:'success',timeout:1000});
                        }).fail(function(xhr, status, error) {
                            $.UIkit.notify("请求失败！", {status:'danger',timeout:2000});
                        });
            },
            updatePerson:function(){
                var json = eval('({' + $("#params").val() + '})');
                $.ajax({
                    url: dekota.url + $("#url").val(),
                    type: 'PUT',//注意在传参数时，加：_method:'PUT'　将对应后台的PUT请求方法
                    dataType: 'json',
                    data: json
                }).done(function(data, status, xhr) {
                            $.UIkit.notify(JSON.stringify(data), {status:'success',timeout:1000});
                        }).fail(function(xhr, status, error) {
                            $.UIkit.notify("请求失败！", {status:'danger',timeout:2000});
                        });
            },
            listPerson:function(){
                var json = eval('({' + $("#params").val() + '})');
                $.ajax({
                    url: dekota.url + $("#url").val(),
                    type: 'PATCH',//注意在传参数时，加：_method:'PATCH'　将对应后台的PATCH请求方法
                    dataType: 'json',
                    data: json
                }).done(function(data, status, xhr) {
                            $.UIkit.notify(JSON.stringify(data), {status:'success',timeout:1000});
                        }).fail(function(xhr, status, error) {
                            $.UIkit.notify("请求失败！", {status:'danger',timeout:2000});
                        });
            }
        };
        window.dekota=(window.dekota)?window.dekota:dekota;
        $(function(){
            dekota.init();
			
        });
    })(window,jQuery);

</script>
</body>
</html>