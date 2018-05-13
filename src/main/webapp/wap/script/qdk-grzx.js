window.onload = function(){
	// 域名
	var url = "https://appapi.qingdouke.com";
	
	// 获取头像
	var faceUrl = ""
	faceUrl =  localStorage.getItem("faceUrl") == "undefined"?"../images/qdk-yuanlogo.png":localStorage.getItem("faceUrl");
	console.log(faceUrl)
	// 获取用户名
	var userName = "";
	userName = localStorage.getItem("nickName") == "undefined"?localStorage.getItem("mobile"):localStorage.getItem("nickName");
	console.log(userName);

	// 设置用户头像
	$(".faceurl").attr("src",faceUrl)
	// 设置用户名称
	$(".username").html(userName);
	// 获取用户Id
	var _passportId = localStorage.getItem("token")?localStorage.getItem("token"):"";
	console.log(_passportId)
	if(_passportId == ""){
		$(".faceurl").attr("src","../images/qdk-yuanlogo.png");
		$(".username").html("昵称");
	}
	// 判断是否登录
	isLogin();
	function isLogin(){
		if(_passportId == ""){
			alert("请先登录");
			location.href = "./qdk-dl.html";
		}else{
			console.log("您已登录");
		}
	}
	// 绑定点击事件
	bindChange()
	function bindChange(){
		// 点击切换账号弹出遮罩
		$(".qiehuan").click(function(){
			$(".bg").show()
		})
		// 点击取消隐藏遮罩
		$(".cancel").click(function(){
			$(".bg").hide()
			$(".grzxlist").slideToggle("fast");
		})
		// 点击去登陆跳转至登录页面
		$(".goLogin").click(function(){
			localStorage.clear();
			location.href="./qdk-dl.html"
		})
		// 点击切换账号隐藏介绍
		$(".cut").click(function(){
			$(".grzxlist").slideToggle("fast");
		})
	}
	// 调用已购买接口
	yetBuy();
	function yetBuy(){
		$.ajax({
			url: url+"/account/post_order_list_authQd.json",
			type:"POST",
	        data: {
	            pageNo : 1,
	            pageSize : 10,
	            passportId : _passportId,
	            type:1
	        },
	        dataType:"json",
	        success:function(res){
	        	console.log(res.body.length)
	        	$(".num").html(res.body.length);
	        }
		})
		
	}
}