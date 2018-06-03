window.onload = function(){
	var userVal = "";
	var passwordVal = "";
	var url = "http://119.84.79.40:8989/";
	var passportId = "";
	var _locaUrl = localStorage.getItem("localUrl");
	
	// 判断_locaUrl值
	if(_locaUrl == null || _locaUrl == "" || _locaUrl == "undefined"){
		_locaUrl = "http://119.84.79.40:8989/"
	}
	console.log(_locaUrl);
	(function bindChange(){
		// 获取用户名
		$(".phone").keyup(function(){
			userVal = $(this).val();
			console.log(userVal);
		})
		// 获取密码
		$(".password").keyup(function(){
			passwordVal = $(this).val();
			console.log(passwordVal);
		})
		// 点击登录
		$(".dl").click(function(){
  		if (userVal != "") {
     		var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
     		isok= reg.test(userVal );
       		if (!isok) {
            	getData();
        	}else{
				getemailData();
        	}
    	};
			
		})
		// 点击微博登录
		$(".bds_tsina").click(function(){
			console.log("微博登录");
			location.href="http://m.qingdouke.com/wap/wbtologin.html?reurl="+_locaUrl;
			localStorage.setItem("threelogin","sina");
		})
		// 点击QQ登录
		$("#qqLoginBtn").click(function(){
			console.log("QQ登录");
			location.href="http://m.qingdouke.com/wap/qqtologin.html?reurl="+_locaUrl;
			localStorage.setItem("threelogin","qq");
		})
	})()
	// 调用手机登录接口
	function getData(){
		$.ajax({
	        url:url+"/user/post_login_by_mobile.json",
	        type:"POST",
	        data:{
	        	mobile:userVal,
	        	password:passwordVal
	        },
	        dataType:"json",
	        success:function(data){
	        	console.log(data)
	        	// alert(data);
	        	if(data.body){
	        		if(data.body.flag == true){
		        		// 本地存储用户id
		        		localStorage.setItem("token", data.body.passportId);
		        		// 本地存储用户手机号
		        		localStorage.setItem("mobile",data.body.mobile);
		        		// 本地存储用户头像
		        		localStorage.setItem("faceUrl",data.body.faceUrl);
		        		// 本地存储用户名
		        		localStorage.setItem("nickName",data.body.nickName);
		        		// 从本地获取用户id
		        		passportId =  localStorage.getItem("token")?localStorage.getItem("token"):"";
						console.log(passportId);
						if(passportId !== "" || passportId !== "undefined"){
							location.href=_locaUrl;
						}
		        	}else{
		        		alert("手机号未注册，请先注册");
		        		location.href = "qdk-zhuce.html";
		        	}
	        	}else{
	        		alert(data.msg);
	        	}
	        },
	        error:function(){
	            console.log("登录失败")
	        }
	    })
	}
	// 调用邮箱登录接口
	function getemailData(){
		$.ajax({
	        url:url+"/user/post_login_by_email.json",
	        type:"POST",
	        data:{
	        	email:userVal,
	        	password:passwordVal
	        },
	        dataType:"json",
	        success:function(data){
	        	console.log(data)
	        	// alert(data);
	        	if(data.body){
	        		if(data.body.flag == true){
		        		// 本地存储用户id
		        		localStorage.setItem("token", data.body.passportId);
		        		// 本地存储用户手机号
		        		localStorage.setItem("mobile",data.body.mobile);
		        		// 本地存储用户头像
		        		localStorage.setItem("faceUrl",data.body.faceUrl);
		        		// 本地存储用户名
		        		localStorage.setItem("nickName",data.body.nickName);
		        		// 从本地获取用户id
		        		passportId =  localStorage.getItem("token")?localStorage.getItem("token"):"";
						console.log(passportId);
						if(passportId !== "" || passportId !== "undefined"){
							location.href="../qdk-shouye.html";
						}
		        	}else{
		        		alert("邮箱未注册，请先注册");
		        		location.href = "qdk-zhuce.html";
		        	}
	        	}else{
	        		alert(data.msg);
	        	}
	        },
	        error:function(){
	            console.log("登录失败")
	        }
	    })
	}
}





