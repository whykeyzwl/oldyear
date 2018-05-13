<%--
  Created by IntelliJ IDEA.
  User: lyra
  Date: 2016/11/30
  Time: 9:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getContextPath();
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css"	href="css/uikit.gradient.min.css"/>
    <link rel="stylesheet" type="text/css"	href="css/notify.gradient.min.css" />
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/uikit.min.js"></script>
    <script type="text/javascript" src="js/notify.min.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.full.js"></script>
    <link rel="stylesheet" href="css/jquery.datetimepicker.css" />
</head>
<body>
<div style="width:800px;margin-top:10px;margin-left: auto;margin-right: auto;text-align: center;">
    <h2>Uikit Test</h2>
</div>
<div style="width:800px;margin-left: auto;margin-right: auto;">
    <fieldset class="uk-form">
        <legend>Uikit系统消息发布测试</legend>
        <div class="uk-form-row">
            消息标题:<input type="text" id="title" class="uk-width-1-1 uk-form-success">
        </div>
        <div class="uk-form-row">
            推送对象:<select id="sendType" class="uk-width-1-1 uk-form-success" onchange="sendTypeChange();">
            <option value="0" selected>所有会员</option>
            <option value="1">模特</option>
            <option value="2">摄影师</option>
            <option value="3">普通会员</option>
            <option value="4">指定会员</option>
            </select>
        </div>
        <div class="uk-form-row" id="sendToDiv">
            推送人id:<input type="text" value="多个ID用英文逗号隔开 例：10000,11111,22222" id="sendTo" class="uk-width-1-1 uk-form-success"
                         onfocus="hideTip()" onblur="showTip()">
        </div>
        <div class="uk-form-row">
            发布时间(不填则立即发布):<input type="text" value="" id="publishTime" name="publishTime" onclick="$('#publishTime').datetimepicker({step: 5});" class="uk-width-1-1 uk-form-success">
        </div>
        <div class="uk-form-row">
            消息内容:<textarea value="" id="content" class="uk-width-1-1 uk-form-success"></textarea>
        </div>
    </fieldset>

    <fieldset class="uk-form">
    <button class="uk-button uk-button-primary uk-button-large" id="btnGet" onclick="submitForm();">保存</button>
    </fieldset>
</div>

<script type="text/javascript">
    function sendTypeChange(){
        if($('#sendType').val()==4){
            $('#sendToDiv').show();
        }
        else
            $('#sendToDiv').hide();
    }

    function showTip(){
        if($('#sendTo').val()=='')
            $('#sendTo').val('多个ID用英文逗号隔开 例：10000,11111,22222');
    }

    function hideTip(){
        if($('#sendTo').val()=='多个ID用英文逗号隔开 例：10000,11111,22222')
            $('#sendTo').val('');
    }

    function submitForm(){
        var noSendObj = $('#sendType').val()!=4 || $('#sendTo').val()=='多个ID用英文逗号隔开 例：10000,11111,22222';
        var json = eval('({'
                + 'title:"'+$('#title').val()+'",'
                + 'content:"'+$('#content').val()+'",'
                + 'sendType:'+$('#sendType').val()+','
                + 'sendTo:"'+(noSendObj?'':$('#sendTo').val())+'",'
                + 'publishTime:"'+$('#publishTime').val()+'"'
                + '})');
        $.ajax({
            url: "http://appapi.qingdouke.com/message/addSysMessage.action",
            type: 'POST',
            dataType: 'json',
            data:json
        }).done(function(data, status, xhr) {
            if(data.code==200)
                $.UIkit.notify("添加成功", {status:'success',timeout:60000});
            else
                $.UIkit.notify(JSON.stringify(data.msg), {status:'error',timeout:60000});
        }).fail(function(xhr, status, error) {
            $.UIkit.notify("请求失败！", {status:'danger',timeout:2000});
        });
    }

    $('#sendToDiv').hide();
</script>
</body>
</html>
