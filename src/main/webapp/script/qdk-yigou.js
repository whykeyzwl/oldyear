window.onload = function(){
	// 参数
	var _pageNo = 1;
	var _pageSize = 10;
	// 域名
	var url = "http://119.84.79.40:8989";
	// 设置html
	var _html = "";
	// 获取passportId
	var _passportId =  localStorage.getItem("token")?localStorage.getItem("token"):"";
	console.log(_passportId);
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
	// 绑定事件
	bindChange();
	function bindChange(){
		// 点击图片跳转详情页
	    $(".picId").live("click",function(){
	        picId = $(this).attr("data-id");
	        console.log(picId);
	        location.href = "qdk-xiangqing.html?picId="+picId;
	    })
	}
	// 调用购买图集接口
	yetBuy();
	function yetBuy(){
		$.ajax({
			url: url+"/account/post_order_list_authQd.json",
			type:"POST",
	        data: {
	            pageNo : _pageNo,
	            pageSize : _pageSize,
	            passportId : _passportId,
	            type:1
	        },
	        dataType:"json",
	        success:function(res){
	        	console.log(res.body.length);
	        	if(res.body.length == 0){
	        		$(".list-bot").html("<div class='noBuy'>您还没有购买图集</div>");
	        	}
	        	for (var i = 0; i < res.body.length; i++) {
	        		_html+='<li>'
                			+'<img src="'+res.body[i].photoAlbumModelCover+'" class="picId" data-id="'+res.body[i].photoAlbumID+'" alt="#">'
                			+'<em></em>'
                			+'<div class="desc"><p class="total">'+res.body[i].modelName+'</p>'
                			+'<p class="time">'+res.body[i].orderDate.split(" ")[0]+'</p></div>'
            			+'</li>';
	        	};
	        	$(".list-bot").html(_html);
	        	$("#loading").hide();
	        },error:function(){
	        	$("#loading").hide();
	        }
		})
	}
	// 上拉加载更多
	$(window).scroll(function(e) {
		scH = $(this).scrollTop();
		winH = $(this).height();
		domH = $(document).height();
		if(scH + winH >= domH){
			_pageNo+=1;
            yetBuy();
            return false;
            console.log("加载了");
		}
	});
	// 下拉刷新
	downRefresh();
	function downRefresh(){
	    window.addEventListener('touchstart',function(event){
	        var touch = event.targetTouches[0];
	        startX = touch.pageX;
	        startY = touch.pageY;
	        // console.log(startX + '，' + startY);
	    },false);
	    //touchmove事件
	    window.addEventListener('touchmove',function(event){
	        var touch = event.targetTouches[0];
	        endX = touch.pageX;
	        endY = touch.pageY;
	    },false);
	    //touchend事件
	    window.addEventListener('touchend',function(event){
	        // console.log(endX + '，' + endY);
	        // console.log((endX - startX) + '，' + (endY - startY));
	        if(endY - startY>25){
	            $(".refresh").css({"display":"block"});
	            setTimeout(function(){
	                $(".refresh").css({"display":"none"});
	            },1000)
	            console.log("刷新了");
	            hotgetmore();
	            newgetmore();
	        }
	    },false); 
	}

}