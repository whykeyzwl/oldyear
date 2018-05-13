var url="http://119.84.79.40:8989/";
function alipayPayData(type,_passportId,_albumId){
	var _pageType=type;
	$.ajax({
		url:url+"/wap/pay/post_alipay_apply_order_auth.json",
		type:"POST",
		data:{
			passportId:_passportId,
			albumId:_albumId,
			albumLevel:1,
			goodsType:1,
			pageType:_pageType
			},
			dataType:"json",
			success:function(data){
				console.log(data);
				window.location.href=data.body;
				$(".mask").hide();
				$(".swiper-slide").css({"-webkit-filter":"blur(0px)"})
				},error:function(){
					
				}
			})
		};