// 域名
var url = "https://appapi.qingdouke.com"
// 调用支付宝支付接口完成支付
function alipayPayData(type){
	var _pageType = type;
	$.ajax({
		url : url+"/wap/pay/post_alipay_apply_order_auth.json",
		type:"POST",
		data:{
			passportId : _passportId,
			albumId : _albumId,
			albumLevel : 1,
			goodsType : 1,
			pageType : _pageType
		},
		dataType:"json",
		success:function(data){
			console.log(data);
			window.location.href = data.body;
			$(".mask").hide();
            $(".swiper-slide").css({"-webkit-filter":"blur(0px)"})
			/*// 创建一个 form 
		  	var form1 = document.createElement("form"); 
		  	form1.id = "form1"; 
		  	form1.name = "form1"; 
		  	// 添加到 body 中 
		  	document.body.appendChild(form1); 
		  	// 创建一个输入 
		  	var input = document.createElement("input"); 
		  	// 设置相应参数 
		  	input.type = "text"; 
		  	input.name = "value1"; 
		  	input.value = "1234567"; 
		  	// 将该输入框插入到 form 中 
		  	form1.appendChild(input); 
		  	// form 的提交方式 
		  	form1.method = "POST"; 
		  	// form 提交路径 
		  	form1.action = data.body; 
		  	// 对该 form 执行提交 
		  	form1.submit(); 
		  	// 删除该 form 
		  	document.body.removeChild(form1);*/ 
		},
		error:function(){

		}
	})
}
// 调用微信支付接口完成支付
function wechatPayData(){
	$.ajax({
		url : url+"/payment/post_wechat_apply_order_auth.json",
		type:"POST",
		data:{
			passportId : _passportId,
			albumId : _albumId,
			goodsType : 1,
			albumLevel : 1

		},
		dataType:"json",
		success:function(data){
			console.log(data)
			weixinJSbridge.invoke("getBrandWCPayRequest",{
				"appId" : res.body.appid,
				"timeStamp" : res.body.timestamp,
				"nonceStr" : res.body.noncestr,
				"package" : "Sign=WXPay",
				"signType" : res.body.sign,
				"paySign" : res.body.sign
			},function(res){
				console.log(res);
			})
		},
		error:function(){

		}
	})
}
