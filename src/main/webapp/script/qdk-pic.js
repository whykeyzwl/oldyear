// 点击图片隐藏页眉页脚定义初始变量
var n = 0;
var _html = "";
// 获取url
var getUrl = window.location.href;
// 获取图集ID
var _albumId = getUrl.split("?")[1].split("&")[0].split("=")[1];
// token
var _passportId = "";
var _openId = "";
// 域名
var url = "http://119.84.79.40:8989";
// 获取当前时间戳
var nowTime = new Date().getTime();
// 用户购买状态
var userHavebuy = "";
// 获取用户token
_passportId = localStorage.getItem("token")?localStorage.getItem("token"):"";
console.log(_passportId);
_openId = localStorage.getItem("openId")?localStorage.getItem("openId"):"";
console.log(_passportId);
// 微信二维码支付刚开始隐藏
$(".QRcode").hide();
var Intval;
// 判断是否支付成功参数
var _ifPay = ""
// 定义用户信息为_user
var _user = "";
// 存储网页链接
var localUrl = "";
localUrl = localStorage.setItem("localUrl", location.href);
// 先获取用户信息判断是否登录
getwechatpassportId();
// 获取当前用户id
function getwechatpassportId(){
	$.ajax({
		url:url+"/wap/getUser.json",
		type:"GET",
		data:{
			ran:nowTime
		},
		success:function(data){
			_user = data;
			console.log(data);
			if(data !== ""){
				// 本地存储用户id
        		localStorage.setItem("token", data.passportId);
        		// 本地存储用户头像
        		localStorage.setItem("faceUrl",data.faceUrl);
        		// 本地存储用户名
        		localStorage.setItem("nickName",data.nickName);
        		// 本地存储微信用户openId（支付）
        		localStorage.setItem("openId",data.openId);
        		// 获取用户信息
        		_passportId = localStorage.getItem("token")?localStorage.getItem("token"):"";
				_openId = localStorage.getItem("openId")?localStorage.getItem("openId"):"";
			}
			
		},
		error:function(res){
			console.log(res);
		}
	})
}
window.onload = function(){
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
	// 禁止页面上下拖动
	$('body').on('touchmove', function(event) {
		event.preventDefault();
	});
	// 判断是否是微博浏览器
	is_wb();
	function is_wb(){
    	var ua = navigator.userAgent.toLowerCase();   
		if (ua.match(/WeiBo/i) == "weibo") {
			window.open("http://119.84.79.40:8989/wap/wbtologin.html");
			console.log("微博授权登陆");
    	}else{
    		console.log("不是微博浏览器a");
    	}
	}
	// 判断是否是微信浏览器
	is_weixin();
	function is_weixin(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
			// 微信浏览器中
			// 弹窗微信支付
			$(".wechatPayIcon").show();
			$(".wechatPayIcon img").css({"margin-right":0});
			// 打赏微信支付
			$(".wechatPay").show();
			// 弹窗支付宝支付
			$(".alipayPayIcon").hide();
			// 打赏支付宝支付
			$(".alipayPay").hide();
			// 普通浏览器微信支付
			$(".browserwechatPay").hide();
	 	}else{
	 		// 普通浏览器中
			// 弹窗微信支付
			$(".wechatPayIcon").hide();
			// 打赏微信支付
			$(".wechatPay").hide();
			// 弹窗支付宝支付
			$(".alipayPayIcon").show();
			// 打赏支付宝支付
			$(".alipayPay").show();
			// 普通浏览器微信支付
			$(".browserwechatPay").show();
		}
	}
	// 用户同意授权登录
	function weixinLogin(){
		if(_user == ""){
			location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcb0f747f6e77fe7f&redirect_uri=http%3A%2F%2F119.84.79.40:18062%2Fwap%2Fwxlogin.html%3Freurl%3Dhttp%3A%2F%2F119.84.79.40:18062%2Fview%2Fqdk-xiangqing.html?picId="+_albumId+"&time="+nowTime+"&response_type=code&scope=snsapi_userinfo&state="+(Math.random()*9+1)*100000+"#wechat_redirect";
			getwechatpassportId();
		}else{
			// 调用微信支付接口
			wechatPay();
			function wechatPay(){
				$.ajax({
					url:url+"/wap/pay/post_wechat_apply_order_auth.json",
					type:"POSt",
					data:{
						passportId : _passportId,
						openId:_openId,
						albumId : _albumId,
						goodsType:1,
						albumLevel:3
					},
					success:function(data){
						console.log(data);
						console.log(data.body.param);
		onBridgeReady();
		function onBridgeReady(){
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
				"appId":data.body.param.appId,
				"timeStamp":data.body.param.timeStamp,
				"nonceStr":data.body.param.nonceStr,
				"package":data.body.param.package,
				"signType":"MD5",//微信签名方式：     
				"paySign":data.body.param.paySign //微信签名 
			},
			function(res){
				if(res.err_msg == "get_brand_wcpay_request:ok" ) {}     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
			}); 
		}
		if (typeof WeixinJSBridge == "undefined"){
			if( document.addEventListener ){
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			}else if (document.attachEvent){
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		}else{
			onBridgeReady();
		}					},
					error:function(res){
						console.log(res);
					}
				})
			}
		}	
	}
	// 获取当前用户id
	
	function getwechatpassportId(){
		$.ajax({
			url:"http://119.84.79.40:8989/wap/getUser.json",
			type:"GET",
			data:{
				ran:newTime
			},
			success:function(data){
				console.log(data);
				// 本地存储用户id
	        	localStorage.setItem("token", data.passportId);
	        	// 本地存储用户头像
	        	localStorage.setItem("faceUrl",data.faceUrl);
	        	// 本地存储用户名
	        	localStorage.setItem("nickName",data.nickName);
	        	// 本地存储微信用户openId（支付）
	        	localStorage.setItem("openId",data.openId);
	        	console.log();
			},
			error:function(res){
				console.log(res);
			}
		})
	}
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
	        	userHavebuy = data.body.buy;
	        	console.log(data);
	        	if(!data.body.userHavebuy){
                    $(".unitNum").html("￥"+data.body.viewprice);
				}else{
					if(data.body.userHavebuy == 1){
                    	$(".unitNum").html("￥"+data.body.viewprice);
	                }else if(data.body.userHavebuy == 0){
	                    $(".unitNum").html("￥1.0");
	                }
				}
	        	for (var i = 0; i < data.body.photos.length; i++) {
	        		_html+='<div class="swiper-slide">'
	        				+'<a>'
	        					+'<img src="'+data.body.photos[i].fullPath+'" alt="#" class="goPic"/>'
                			+'</a>'
            			+'</div>'
	        	};
            	$(".swiper-wrapper").html(_html);
	        },
	        error:function(){
	            console.log("获取数据失败");
	        }
		})
	}
	// 微信二维码支付
	function QRcodePay(){
		$.ajax({
			url:url+"/wap/scanCode/post_wechat_apply_order_auth.json",
			type:"POST",
			data:{
				passportId: _passportId,
				albumId: _albumId,
				goodsType: 1,
				albumLevel: 3
			},
			success:function(data){
				console.log(data.msg);
				if(data.msg == "OK"){
					$(".QRcode").show();
					$(".QRcode img").attr("src",url+"/wap/scanCode/makeQrCode.do?code_url="+data.body.code_url);
					Intval = setInterval(ifPay,2000);
				}else if(data.msg == "未登录" || data.msg == "请登录"){
					location.href = "qdk-dl.html";
				}else{
					alert(data.msg);
				}
			},
			error:function(res){
				alert(res.msg);
			}
		})
	}
	// 定时器判断是否支付
	function ifPay(){
		$.ajax({
			url: url + "/photoalbum/post_albumDitail.json",
			type: "POST",
			data: {
				albumId: _albumId,
				passportId: _passportId
			},
			dataType: "json",
			success:function(data){
				console.log(data.body.buy);
				_ifPay = data.body.buy;
				if(_ifPay == 1){
					console.log("定时器");
					clearInterval(Intval);
					window.location.reload();
        			
				}
			},
			error:function(res){
				console.log(res);
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
		// 点击突出浏览模式
		$(".header").click(function(){
			location.href="./qdk-xiangqing.html?picId="+_albumId;
			// wechatPayData()
		})
		// 点击微信支付
		$(document).on('touchend','img.wechatPay',function(){
			weixinLogin();
		}); 
		// 点击支付宝支付
		$(".alipayPay").live("click",function(){
			if (_passportId == "" || _passportId == "undefined") {
				location.href = "qdk-dl.html";
				return false;
			} else {
				if (userHavebuy == 0) {
					$.ajax({
		                url:url+"/wap/pay/post_alipay_apply_order_auth.json",
		                type:"POST",
		                data:{
							passportId:_passportId,
							albumId:_albumId,
							albumLevel:3,
							goodsType:1,
							pageType:1
			            },
			            dataType:"json",
			            success:function(data){
							
							if (data.body == "" || data.body == "undefined" || data.body == undefined) {
							    location.href = "qdk-dl.html";	
							}else{
								window.location.href=data.body;
								$(".mask").hide();
								$(".swiper-slide").css({"-webkit-filter":"blur(0px)"})
							}
							
						},error:function(){
								
						}
					});
				} else {
					alert("您已购买");
				}
			}
		})
		// 普通浏览器点击微信支付
		$(".browserwechatPay").live("click",function(){
			QRcodePay();
		})
		// 点击二维码弹框隐藏
		$(".QRcode").live("click",function(){
			$(this).hide();
		})
	}
}