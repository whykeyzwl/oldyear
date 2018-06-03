window.onload = function(){
	// 域名
	var url = "https://appapi.qingdouke.com";
	// 获取url
	var getUrl = window.location.href
	// 通过url获取注册手机号
	var _zhuce2phone= getUrl.split("?")[1].split("&")[0].split("=")[1];
	// 通过url获取验证码
	var _zhuce2yzm = getUrl.split("?")[1].split("&")[1].split("=")[1];
	// 密码
	var _zhuce2passwore = "";
	console.log(_zhuce2phone+","+_zhuce2yzm)
	// 绑定事件
	bindChange();
	function bindChange(){
		// 获取密码框内密码
		$(".zhuce2password").keyup(function(){
			_zhuce2passwore = $(this).val();
			console.log(_zhuce2passwore);
		})
		// 点击提交
		$(".submit").click(function(){
			$.ajax({
				url : url+"/user/post_register_byMobile.json",
				type:"POST",
		        data:{
		        	mobile : _zhuce2phone,
		        	securityCode : _zhuce2yzm,
		        	password : _zhuce2passwore
		        },
		        dataType:"json",
		        success:function(data){
		        	console.log(data)
		        	if(data.msg == "OK" || data.msg == "手机号已注册，请直接登录"){
		        		alert(data.msg);
		        		location.href="qdk-dl.html"
		        	}else{
		        		alert(data.msg);
		        	}
		        },
		        error:function(){
		            console.log("发送失败")
		        }
			})
		})
	}

}