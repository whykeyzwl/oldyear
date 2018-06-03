// 最新最热初始变量
var _hothtml = "";
var _newhtml = "";
// 参数
var pageNo = 1;
var pageSize = 4;
var typeLevel = 3;
// 图片标题
var _picTitle = "";
// 查看数量
var _seeNum = "";
// 获取url
var getUrl = window.location.href
// 获取图集ID
var _albumId = getUrl.split("?")[1].split("=")[1];
// token
var _passportId = "";
// 域名
var url = "https://appapi.qingdouke.com";
// 渲染内容
var _html = "";
// 支付状态
var getalipay = "";
// 获取用户token
_passportId =  localStorage.getItem("token")?localStorage.getItem("token"):"";
window.onload = function(){
	console.log(_passportId);
	// 判断是否登录
	function isLogin(){
		if(_passportId == ""){
			alert("请先登录");
			location.href = "./qdk-dl.html";
		}else{
			console.log("您已登录");
		}
	}
	// 绑定事件
	bindChange();
	function bindChange(){
		// 点击图片跳转查看图片页面
		$(".goPic").live("click",function(){
			location.href = "./qdk-pic.html?albumId="+_albumId
		})
		// 点击微信支付
		$(".wechatPay").live("click",function(){
			isLogin();
			wechatPayData();
		})
		// 点击支付宝支付
		$(".alipayPay").live("click",function(){
			isLogin();
			$.getScript("../lib/main.js",function(data){
				alipayPayData(1);
			})
		})
		// 点击“最热”
	    $(".hoter").click(function(){
	        console.log("最热")
	        _newhtml = ""
	        hotgetmore()
	        $(".hotView").show().siblings().hide();
	    })
	    // 点击“最新”
	    $(".newer").click(function(){
	        console.log("最新")
	        $(".newView").show().siblings().hide();
	        newgetmore();
	    })
	    // 点击图片跳转详情页
	    $(".picId").live("click",function(){
	        picId = $(this).attr("data-id");
	        console.log(picId);
	        location.href = "qdk-xiangqing.html?picId="+picId
	    })
	}
	// 调用接口，渲染swiper图片
	swiperData();
	function swiperData(){
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
	        	_picTitle = data.body.title;
	        	_seeNum = data.body.viewCount
	        	for (var i = 0; i < data.body.photos.length; i++) {
	        		_html+='<div class="swiper-slide">'
	        				+'<a href="#" >'
	        					+'<img src="'+data.body.photos[i].fullPath+'" alt="#" class="goPic"/>'
                			+'</a>'
            			+'</div>'
	        	};
            	$(".swiper-wrapper").html(_html);
            	$(".picTitle").html(_picTitle);
            	$(".seeNum").html(_seeNum);
	        },
	        error:function(){
	            console.log("登录失败")
	        }
		})
	}
	// 调用最热图集接口
	function hotgetmore(){
	    // 调用接口获取数据
	    $.ajax({
	        url:url+"/index/get_home_albumHot.json?pageNo="+pageNo+"&pageSize="+pageSize+"&typeLevel="+typeLevel,
	        type:"POST",
	        dataType:"json",
	        beforeSend:function(XMLHttpRequest){
	            $("#loading").show();
	        },
	        success:function(data){
	            for(i=0;i<data.body.length;i++){
	                _hothtml+='<li>'
	                       +'<img src="'+data.body[i].cover+'" alt="'+data.body[i].modelName+'" data-id="'+data.body[i].id+'" class="picId">'
	                       +'<em></em><div class="desc"><p class="time">'+data.body[i].lastUpdateTime.split(" ")[0]+'</p>'
	                       +'<p class="total"><i></i>'+data.body[i].viewCount+'</p><div>'
	                       +'</li>';
	                $(".hotView").html(_hothtml);
	                $("#loading").hide();
	            }
	        },
	        error:function(){
	            $("#loading").hide();
	        }
	    })
	}
	hotgetmore();
	// 调用最新图集接口
	function newgetmore(){
	    $.ajax({
	        url: url+"/index/get_home_album.json",
	        type:"POST",
	        data:{
	            pageNo : pageNo,
	            pageSize : pageSize,
	            typeLevel : typeLevel
	        },
	        success:function(data){
	            console.log(data)
	            for(i=0;i<data.body.length;i++){
	                _newhtml+='<li>'
	                       +'<img src="'+data.body[i].cover+'" alt="'+data.body[i].modelName+'" data-id="'+data.body[i].id+'" class="picId">'
	                       +'<em></em><p class="time">'+data.body[i].lastUpdateTime.split(" ")[0]+'</p>'
	                       +'<p class="total"><i></i>'+data.body[i].viewCount+'</p>'
	                       +'</li>';
	                $(".newView").html(_newhtml);
	                $("#loading").hide();
	            }
	        },
	        error:function(){
	            $("#loading").hide();
	        }
	    })
	}
	// 上拉加载更多
	$(window).scroll(function(e){
	    scH=$(this).scrollTop();
	    winH=$(this).height();
	    domH=$(document).height();
	    if(scH+winH>=domH){
	        //$("#loading").show();
	        hotgetmore();
	        newgetmore();
	    }
	});
}









