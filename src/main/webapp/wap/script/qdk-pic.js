// 点击图片隐藏页眉页脚定义初始变量
var n = 0;
var _html = "";
// 获取url
var getUrl = window.location.href
// 获取图集ID
var _albumId = getUrl.split("?")[1].split("=")[1];
// token
var _passportId = "";
// 域名
var url = "https://appapi.qingdouke.com";
// 获取用户token
_passportId =  localStorage.getItem("token")?localStorage.getItem("token"):"";
console.log(_passportId)

window.onload = function(){
	console.log(_passportId)
	// 判断用户是否登录
	function isLogin(){
		if(_passportId == ""){
			// alert("请先登录");
			location.href = "qdk-dl.html";
		}else{
			console.log("您已登录");
		}
	}
	// 调用接口渲染图片
	getData();
	function getData(){
		$.ajax({
			url : url+"/photoalbum/post_albumDitail.json",
			type:"POST",
	        data:{
	        	albumId : _albumId,
                passportId : _passportId
	        },
	        dataType:"json",
	        success:function(data){
	        	console.log(data);
	        	for (var i = 0; i < data.body.photos.length; i++) {
	        		_html+='<div class="swiper-slide">'
	        				+'<a href="#" >'
	        					+'<img src="'+data.body.photos[i].fullPath+'" alt="#" class="goPic"/>'
                			+'</a>'
            			+'</div>'
	        	};
            	$(".swiper-wrapper").html(_html);
	        },
	        error:function(){
	            console.log("获取数据失败")
	        }
		})
	}
	// 绑定事件
	bindChange();
	function bindChange(){
		// 点击图片隐藏页眉页脚
		$(".swiper-wrapper").live("click",function(){
			if(!n){
				$(".main").siblings().slideUp(300);
				n = 1;
			}else{
				$(".main").siblings().slideDown(300);
				n = 0;
			}
		})
		$(".header").click(function(){
			location.href=document.referrer;
			// wechatPayData()
		})
		$(".alipayPay").live("click",function(){
			isLogin();
			$.getScript("../lib/main.js",function(data){
				alipayPayData(2);
				setTimeout(function(){
					window.location.href="./qdk-xiangqing.html?picId="+_albumId;
				},2000)
			})
		})
		// 点击微信分享
		$(".shareWechat").click(function(){
			console.log("分享微信")
			wx.onMenuShareAppMessage({
				title:"青豆客",
				desc:"光天化日，美女模特竟和摄影师做出这种事！",
				link:"www.qingdouke.com",
				imgUrl:"",
				type:"",
				dataUrl:"",
				success:function(){
					console.log("分享到微信成功");
				},cancel:function(){
					console.log("分享到微信失败");
				}
			})
		})
	}
	// 分享到
	

}