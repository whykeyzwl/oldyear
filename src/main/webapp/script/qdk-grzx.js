window.onload = function(){
	// 域名
	var url = "http://119.84.79.40:8989/";
	// 获取当前时间戳
	var nowTime = new Date().getTime();
	// 获取头像
	var faceUrl = "";
	faceUrl =  localStorage.getItem("faceUrl") == "undefined"?"../img/qdk-yuanlogo.png":localStorage.getItem("faceUrl");
	console.log(faceUrl);
	// 获取用户名
	var userName = "";
	userName = localStorage.getItem("nickName") == "undefined"?localStorage.getItem("mobile"):localStorage.getItem("nickName");
	console.log(userName);

	// 设置用户头像
	$(".faceurl").attr("src",faceUrl);
	// 设置用户名称
	$(".username").html(userName);
	// 获取用户Id
	var _passportId = localStorage.getItem("token")?localStorage.getItem("token"):"";
	console.log(_passportId)
	if(_passportId == "" || _passportId == "undefined"){
		$(".faceurl").attr("src","../img/qdk-yuanlogo.png");
		$(".username").html("昵称");
	}
	// 判断用户手机是iphone还是android
    function ismobile(test){
        var u = navigator.userAgent, 
            app = navigator.appVersion;
        if(/AppleWebKit.*Mobile/i.test(navigator.userAgent) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))){
            if(window.location.href.indexOf("?mobile")<0){
                try{
                    if(/iPhone|mac|iPod|iPad/i.test(navigator.userAgent)){
                        return '0';
                    }else{
                        return '1';
                    }
                }catch(e){}
            }
        }else if( u.indexOf('iPad') > -1){
            return '0';
        }else{
            return '1';
        }
    };
    var pla=ismobile(1);
    console.log(pla);
    isPhone();
    function isPhone(){
        if(pla == 0){
            // 苹果手机
            $(".downLoadAPP").click(function(){
                alert("敬请期待！");
            })
        }else if(pla == 1){
            // 安卓手机
            $(".downLoadAPP").click(function(){
                $(".downLoadAPP").attr("href","http://www.qingdouke.com/app/app-qingdouke_00-release.apk");
            })
        }
    }
	// 判断是否登录
	isLogin();
	function isLogin(){
		if(_passportId == ""){
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
			$(".bg").show();
			$(".grzxlist").fadeOut();
		})
		// 点击取消隐藏遮罩
		$(".cancel").click(function(){
			$(".bg").hide();
			$(".grzxlist").fadeIn();
		})
		// 点击去登陆跳转至登录页面
		$(".goLogin").click(function(){
			localStorage.clear();
			location.href="./qdk-dl.html";
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
			url: url+"/account/post_order_list_authQdSize.json",
			type:"POST",
	        data: {
	            pageNo : 1,
	            pageSize : 10,
	            passportId : _passportId,
	            type:1
	        },
	        dataType:"json",
	        success:function(res){
	        	console.log(res);
	        	$(".num").html(res.body);
	        }
		})
		
	}
}