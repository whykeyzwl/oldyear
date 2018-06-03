// 最新最热初始变量
var _hothtml = "";
var _newhtml = "";
// 参数
var pageNo = 1;
var pageSize = 8;
var typeLevel = 2;
// 图片标题
var _picTitle = "";
// 查看数量
var _seeNum = "";
// 获取url
var getUrl = window.location.href;
// 获取图集ID
/*var _albumId = getUrl.toLowerCase().match(/(^|\\?|&)picid=([^&]*)(\\s|&|$)/)[2];*/
var _albumId = window.location.href.split("?")[1].split("&")[0].split("=")[1];
// token
var _passportId = "";
var _openId = "";
// 域名
var url = "http://119.84.79.40:8989";
// 渲染内容
var _html = "";
// 用户购买状态
var userHavebuy = "";
// 支付状态
var getalipay = "";
// 获取当前时间戳
var nowTime = new Date().getTime();
// 分享后显示地址
var link = window.location.href + "&time=" + nowTime;
// 微信登录用户id
var wechatPassportId = "";
var w = 0;
// 判断是否支付成功参数
var _ifPay = ""
console.log(link);
// 获取用户token
_passportId = localStorage.getItem("token") ? localStorage.getItem("token") : "";
_openId = localStorage.getItem("openId") ? localStorage.getItem("openId") : "";
// 微信二维码支付刚开始隐藏
$(".QRcode").hide();
// 定义用户信息为_user
var _user = "";
var Intval;
// 存储网页链接
var localUrl = "";
localUrl = localStorage.setItem("localUrl", location.href);
// 先获取用户信息判断是否登录
getwechatpassportId();
// 获取当前用户id
function getwechatpassportId() {
	$.ajax({
		url: url + "/wap/getUser.json",
		type: "GET",
		data: {
			ran: nowTime
		},
		success: function(data) {
			_user = data;
			console.log("'"+data+"'如果为空证明不是微信登录");
			if (data !== "") {
				// 本地存储用户id
				localStorage.setItem("token", data.passportId);
				// 本地存储用户头像
				localStorage.setItem("faceUrl", data.faceUrl);
				// 本地存储用户名
				localStorage.setItem("nickName", data.nickName);
				// 本地存储微信用户openId（支付）
				localStorage.setItem("openId", data.openId);
				// 获取用户信息
				_passportId = localStorage.getItem("token") ? localStorage.getItem("token") : "";
				_openId = localStorage.getItem("openId") ? localStorage.getItem("openId") : "";
			}
		},
		error: function(res) {
			console.log(res);
		}
	})
}
window.onload = function() {
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
		
	// 判断是否是微博浏览器
	is_wb();
	function is_wb(){
		var ua = navigator.userAgent.toLowerCase();
		if (ua.match(/WeiBo/i) == "weibo") {
			window.open("http://119.84.79.40:8989/wap/wbtologin.html?reurl=" + window.location.href);
		} else {
			console.log("不是微博浏览器a");
		}
	}

	// 判断是否是微信浏览器
	is_weixin();

	function is_weixin() {
		var ua = navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == "micromessenger"){
			// 微信浏览器中
			// 弹窗微信支付
			$(".wechatPayIcon").show();
			$(".wechatPayIcon img").css({"margin-right":0});
			// 打赏微信支付
			$(".wechatPay").show();
			// 弹窗支付宝支付
			$(".alipayPayIcon").hide();
			// 打赏支付宝支付
			$(".alipayPayBtn").hide();
			// 普通浏览器微信支付
			$(".browserwechatPay").hide();
		} else {
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
	// 判断是否登录
	function isLogin() {
		var ua = navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == "micromessenger"){
			if (_user == "") {
				location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcb0f747f6e77fe7f&redirect_uri=http%3A%2F%2F119.84.79.40:18062%2Fwap%2Fwxlogin.html%3Freurl%3Dhttp%3A%2F%2F119.84.79.40:18062%2Fview%2Fqdk-xiangqing.html?picId=" + _albumId + "&time=" + nowTime + "&response_type=code&scope=snsapi_userinfo&state=" + (Math.random() * 9 + 1) * 100000 + "#wechat_redirect";
				getwechatpassportId();
			}
		}else{
			if (_passportId == "") {
				location.href = "qdk-dl.html";
				return false;
			} else {
				console.log("您已登录");
			}
		}
	}
	// 微信用户同意授权登录

	function weixinLogin() {
		if (_user == "") {
			location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcb0f747f6e77fe7f&redirect_uri=http%3A%2F%2F119.84.79.40:18062%2Fwap%2Fwxlogin.html%3Freurl%3Dhttp%3A%2F%2F119.84.79.40:18062%2Fview%2Fqdk-xiangqing.html?picId=" + _albumId + "&time=" + nowTime + "&response_type=code&scope=snsapi_userinfo&state=" + (Math.random() * 9 + 1) * 100000 + "#wechat_redirect";
			getwechatpassportId();
		} else {
			// 调用微信支付接口
			wechatPay()

			function wechatPay() {
				$.ajax({
					url: url + "/wap/pay/post_wechat_apply_order_auth.json",
					type: "POSt",
					data: {
						passportId: _passportId,
						openId: _openId,
						albumId: _albumId,
						goodsType: 1,
						albumLevel: 3
					},
					success: function(data) {
						console.log(data);
						console.log(data.body.param);
						onBridgeReady();

						function onBridgeReady() {
							WeixinJSBridge.invoke('getBrandWCPayRequest', {
									"appId": data.body.param.appId,
									"timeStamp": data.body.param.timeStamp,
									"nonceStr": data.body.param.nonceStr,
									"package": data.body.param.package,
									"signType": "MD5", //微信签名方式：     
									"paySign": data.body.param.paySign //微信签名 
								},
								function(res) {

									if (res.err_msg == "get_brand_wcpay_request:ok") {} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 

								});
						}
						if (typeof WeixinJSBridge == "undefined") {
							if (document.addEventListener) {
								document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
							} else if (document.attachEvent) {
								document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
								document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
							}
						} else {
							onBridgeReady();
						}
					},
					error: function(res) {
						console.log(res);
					}
				})
			}
		}
	}
	// 获取当前用户id

	function getwechatpassportId() {
		$.ajax({
			url: url + "/wap/getUser.json",
			type: "GET",
			data: {
				ran: nowTime
			},
			success: function(data) {
				console.log(data);
				// 本地存储用户id
				localStorage.setItem("token", data.passportId);
				// 本地存储用户头像
				localStorage.setItem("faceUrl", data.faceUrl);
				// 本地存储用户名
				localStorage.setItem("nickName", data.nickName);
				// 本地存储微信用户openId（支付）
				localStorage.setItem("openId", data.openId);
			},
			error: function(res) {
				console.log(res);
			}
		})
	}
	console.log("passportId : " + _passportId);
	console.log("openId : " + _openId);
	console.log("上一行是undefined证明不是微信登录")

	
	// 绑定事件
	bindChange();

	function bindChange(){
		// 点击图片跳转查看图片页面
		$(".goPic").live("click", function() {
			location.href = "http://119.84.79.40:8989/view/qdk-pic.html?albumId=" + _albumId+"&time="+new Date().getTime();
		})
		// 点击微信支付
		$(document).on('touchend', 'img.wechatPayPic', function() {
			weixinLogin();
		});
		$(document).on('touchend', 'p.wechatPayBtn', function() {
			weixinLogin();
		});
		// 点击支付宝支付
		$(".alipayPayPic").live("click", function() {
		
			if (_passportId == "" || _passportId == "undefined") {
				
				location.href = "qdk-dl.html";
				
			} else {
				
				if (userHavebuy == 0) {
					//$.getScript("../lib/main.js", function(data) {
					//	alipayPayData(1,_passportId,_albumId);
					//});
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
				}else{
					alert('已登录');
								
				}
								
			}
		});
		// 普通浏览器点击打赏按钮
		$(".alipayPayBtn").live("click",function(){
			if (userHavebuy == 0) {
				$(".mask").show();
				$(".mask").css({"position":"absolute","top":"5rem","left":"1.1rem"})
			}
			
		})
		// 点击“最热”
		$(".hoter").click(function() {
			console.log("最热");
			pageNo = 1;
			_hothtml = "";
			_newhtml = "";
			hotgetmore();
			$(".hotView").show().siblings().hide();
		})
		// 点击“最新”
		$(".newer").click(function() {
			console.log("最新");
			pageNo = 1;
			$(".newView").show().siblings().hide();
			_hothtml = "";
			_newhtml = "";
			newgetmore();
		})
		// 点击图片跳转详情页
		$(".picId").live("click", function() {
			// 获取图片id
			picId = $(this).attr("data-id");
			location.href = "qdk-xiangqing.html?picId=" + picId+"&time="+new Date().getTime();
		})
		// 点击普通浏览器微信支付弹出二维码
		$(".browserwechatPay").live("click",function(){
			QRcodePay();
		})
		// 点击二维码弹框隐藏
		$(".QRcode").live("click",function(){
			$(this).hide();
		})
	}
	// 调用接口，渲染swiper图片
	swiperData();
	function swiperData() {
		$.ajax({
			url: url + "/photoalbum/post_albumDitail.json",
			type: "POST",
			data: {
				albumId: _albumId,
				passportId: _passportId
			},
			dataType: "json",
			success: function(data) {
				console.log(data);
				userHavebuy = data.body.buy;
				_picTitle = data.body.title;
				_seeNum = data.body.viewCount;
				if (userHavebuy == 0) {
					$(".alipayPayBtn").html("打赏");
					$(".wechatPayBtn").html("打赏");
				} else {
					$(".alipayPayBtn").html("已购").css({"background":"#ccc"});
					$(".wechatPayBtn").html("已购").css({"background":"#ccc"});
				}
				if(!data.body.userHavebuy){
					$(".fixedMoney").html("￥"+data.body.viewprice);
                    $(".unitNum").html("￥"+data.body.viewprice);
				}else{
					if(data.body.userHavebuy == 1){
	                    $(".fixedMoney").html("￥"+data.body.viewprice);
                    	$(".unitNum").html("￥"+data.body.viewprice);
	                }else if(data.body.userHavebuy == 0){
	                    $(".fixedMoney").html("￥1.0");
	                    $(".unitNum").html("￥1.0");
	                }
				}
				
				for (var i = 0; i < data.body.photos.length; i++) {
					_html += '<div class="swiper-slide">' + '<a>' + '<img src="' + data.body.photos[i].fullPath + '" alt="#" class="goPic"/>' + '</a>' + '</div>'
				};
				$(".swiper-wrapper").html(_html);
				$(".picTitle").html(_picTitle);
				$(".seeNum").html(_seeNum);
			},
			error: function() {
				console.log("登录失败");
			}
		})
	}
	// 调用最热图集接口
	function hotgetmore() {
		// 调用接口获取数据
		$.ajax({
			url: url + "/index/get_home_albumHot.json?pageNo=" + pageNo + "&pageSize=" + pageSize + "&typeLevel=" + typeLevel,
			type: "POST",
			dataType: "json",
			beforeSend: function(XMLHttpRequest) {
				$("#loading").show();
			},
			success: function(data) {
				for (i = 0; i < data.body.length; i++) {
					_hothtml += '<li>' + '<img src="' + data.body[i].cover + '" alt="' + data.body[i].modelName + '" data-id="' + data.body[i].id + '" class="picId">' + '<em></em><div class="desc"><p class="time">' + data.body[i].title + '</p>' + '<p class="total"><i></i>' + data.body[i].viewCount + '</p><div>' + '</li>';
				}
				$(".hotView").html(_hothtml);
				$("#loading").show();
			},
			error: function() {
				$("#loading").hide();
			}
		})
	}
	hotgetmore();
	// 调用最新图集接口
	function newgetmore() {
		$.ajax({
			url: url + "/index/get_home_album.json",
			type: "POST",
			data: {
				pageNo: pageNo,
				pageSize: pageSize,
				typeLevel: typeLevel
			},
			success: function(data) {
				console.log(data);
				for (i = 0; i < data.body.length; i++) {
					_newhtml += '<li>' + '<img src="' + data.body[i].cover + '" alt="' + data.body[i].modelName + '" data-id="' + data.body[i].id + '" class="picId">' + '<em></em><div class="desc"><p class="time">' + data.body[i].title + '</p>' + '<p class="total"><i></i>' + data.body[i].viewCount + '</p></div>' + '</li>';
				}
				$(".newView").html(_newhtml);
				$("#loading").show();
			},
			error: function() {
				$("#loading").hide();
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
	// 上拉加载更多
	$(window).scroll(function(e) {
		scH = $(this).scrollTop();
		winH = $(this).height();
		domH = $(document).height();
		if(scH + winH >= domH){
			pageNo+=1;
            hotgetmore();
            newgetmore();
            return false;
            console.log("加载了");
		}
	});
}