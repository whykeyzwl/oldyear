window.onload = function(){
	var _zhucePhone = "";
	var url = "https://appapi.qingdouke.com";
	var type =1;	 // type为1是注册
	var phoneyzm = "";
	// 绑定事件
	(function bindChange(){
		// 获取注册时的手机号
		$(".zhucePhone").keyup(function(){
			_zhucePhone = $(this).val();
			console.log(_zhucePhone);
		})
		// 点击向手机发送验证码
		$(".zhuceSendyzm").click(function(){
			zhuceSendyzm();
		})
		// 点击下一步验证验证码
		$(".next").click(function(){
			yanzhengyzm();
			// 将注册的手机号存储到本地
			if (window.localStorage) {
			    localStorage.setItem("zhucephone", _zhucePhone);	
			} else {
			    Cookie.write("zhucephone", _zhucePhone);	
			}
		})
		// 获取输入的验证码
		$(".phoneyzm").keyup(function(){
			phoneyzm = $(this).val();
		})
	})()
	// 调用注册发送验证码接口
	function zhuceSendyzm(){
		$.ajax({
			url : url+"/user/post_sendSecurityCode.json",
			type:"POST",
	        data:{
	        	mobile:_zhucePhone,
	        	type:1
	        },
	        dataType:"json",
	        success:function(data){
	        	console.log(data)
	        	if(data.msg != "OK"){
	        		alert(data.msg);
	        	}
	        },
	        error:function(){
	            console.log("发送失败")
	        }
		})
	}
	// 调用接口验证验证码
	function yanzhengyzm(){
		$.ajax({
			url : url+"/user/post_verifySecurityCode.json",
			type:"POST",
	        data:{
	        	mobile:_zhucePhone,
	        	securityCode:phoneyzm,
	        	type:1
	        },
	        dataType:"json",
	        success:function(data){
	        	console.log(data)
	        	if(data.msg == "OK"){
	        		location.href="qdk-zhuce2.html?_zhucePhone="+_zhucePhone+"&phoneyzm="+phoneyzm
	        	}else{
	        		alert(data.msg);
	        	}
	        	
	        },
	        error:function(){
	            console.log("发送失败")
	        }
		})
	}
	/*// 发送验证码倒计时
	function count(){
		$('#btn').click(function () {
			var count = 10;
			var countdown = setInterval(CountDown, 1000);
			function CountDown() {
				$("#btn").attr("disabled", true);
				$("#btn").val("Please wait " + count + " seconds!");
				if (count == 0) {
					$("#btn").val("Submit").removeAttr("disabled");
					clearInterval(countdown);
				}
				count--;
			}
		})
	}*/
}