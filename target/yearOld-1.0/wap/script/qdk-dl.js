window.onload = function(){
	var userVal = "";
	var passwordVal = "";
	var url = "https://appapi.qingdouke.com";
	var passportId = "";

	(function bindChange(){
		// 获取用户名
		$(".phone").keyup(function(){
			userVal = $(this).val();
			console.log(userVal);
		})
		// 获取密码
		$(".password").keyup(function(){
			passwordVal = $(this).val();
			console.log(passwordVal)
		})
		// 点击登录
		$(".dl").click(function(){
			getData();
		})
		// 点击微博登录
		$(".bds_tsina").click(function(){
			console.log("微博登录");
			localStorage.setItem("threelogin","sina");
		})
		// 点击QQ登录
		$("#qqLoginBtn").click(function(){
			console.log("QQ登录");
			localStorage.setItem("threelogin","qq");
		})
	})()
	// 调用登录接口
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
	        	if(data.msg == "OK"){
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
	            	location.href="../qdk-shouye.html"
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





