window.onload = function(){
	// 域名
	var url = "http://119.84.79.40:8989";
	// 手机号
	var _zhmmPhone = "";
	// 验证码
	var _zhmmYzm = "";
	// 绑定事件
	bindChange();
	function bindChange(){
		// 获取手机号
		$(".zhmmPhone").keyup(function(){
			_zhmmPhone = $(this).val();
			console.log(_zhmmPhone);
		})
		// 获取验证码
		$(".zhmmYzm").keyup(function(){
			_zhmmYzm = $(this).val();
			console.log(_zhmmYzm);
		})
		// 点击发送验证码
		$(".zhmmSendyzm").click(function(){
			zhmmSendyzm();
		})
		// 点击下一步验证验证码
		$(".next").click(function(){
			yanzhengyzm();
		})
	}
	// 调用发送验证码接口
	function zhmmSendyzm(){
		$.ajax({
			url : url+"/user/post_sendSecurityCode.json",
			type:"POST",
	        data:{
	        	mobile:_zhmmPhone,
	        	type:4
	        },
	        dataType:"json",
	        success:function(data){
	        	console.log(data);
	        	if(data.msg != "OK"){
	        		alert(data.msg);
	        	}else{
	        		var count = 60;
					var countdown = setInterval(CountDown, 1000);
					function CountDown() {
						$("#btn").attr("disabled", true);
						$("#btn").val(count+"s");
						if (count == 0) {
							$("#btn").val("重新发送").removeAttr("disabled");
							clearInterval(countdown);
						}
						count--;
					}
	        	}
	        },
	        error:function(){
	            console.log("发送失败");
	        }
		})
	}
	// 调用接口验证验证码
	function yanzhengyzm(){
		$.ajax({
			url : url+"/user/post_verifySecurityCode.json",
			type:"POST",
	        data:{
	        	mobile:_zhmmPhone,
	        	securityCode:_zhmmYzm,
	        	type:4
	        },
	        dataType:"json",
	        success:function(data){
	        	console.log(data);
	        	if(data.msg == "OK"){
	        		location.href="qdk-zhmm2.html?_zhmmPhone="+_zhmmPhone+"&_zhmmYzm="+_zhmmYzm;
	        	}else{
	        		alert(data.msg);
	        	}
	        	
	        },
	        error:function(){
	            console.log("发送失败");
	        }
		})
	}
}