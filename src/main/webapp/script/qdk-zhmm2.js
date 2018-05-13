window.onload = function(){
	// 域名
	var url = "http://119.84.79.40:8989";
	var getUrl = window.location.href;
	// 第一次密码
	var _newPassword = "";
	// 第二次密码
	var _newPassword2 = "";
	// 获取手机号
	var _zhmmphone = getUrl.split("?")[1].split("&")[0].split("=")[1];
	console.log(_zhmmphone);
	// 绑定事件
	bindChange();
	function bindChange(){
		// 获取第一次输入的新密码
		$(".newPassword").keyup(function(){
			_newPassword = $(this).val();
			console.log(_newPassword);
		})
		// 获取第二次输入的新密码
		$(".newPassword2").keyup(function(){
			_newPassword2 = $(this).val();
			console.log(_newPassword2);
		})
		// 点击提交验证提交
		$(".next").click(function(){
			if(_newPassword == ""){
				alert("请输入新密码")
			}else if(_newPassword2 == ""){
				alert("请再次输入新密码")
			}else if(_newPassword !== _newPassword2){
				alert("两次输入不一致");
			}else{
				changePassword()
			}
		})
	}
	// 修改密码调用接口
	function changePassword(){
		$.ajax({
			url : url+"/user/post_refindPassowrd.json",
			type:"POST",
			data:{
				password : _newPassword,
				mobile : _zhmmphone
			},
			success:function(data){
				console.log(data);
				if(data.msg == "OK"){
					alert(data.body.msg)
					location.href = "./qdk-dl.html";
				}else if(data.msg="验证码已过期，请重新生成"){
					alert(data.msg);
					location.href = "./qdk-zhmm1.html";
				}
			}
		})
	}

}