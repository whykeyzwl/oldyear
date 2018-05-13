window.onload = function(){
	// 域名
	var url = "https://appapi.qingdouke.com";
	// 设置html
	var _html = "";
	// 获取passportId
	var _passportId =  localStorage.getItem("token")?localStorage.getItem("token"):"";
	console.log(_passportId);
	// 绑定事件
	bindChange();
	function bindChange(){
		// 点击图片跳转详情页
	    $(".picId").live("click",function(){
	        picId = $(this).attr("data-id");
	        console.log(picId);
	        location.href = "qdk-xiangqing.html?picId="+picId
	    })
	}
	// 调用购买图集接口
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
	        	console.log(res);
	        	for (var i = 0; i < res.body.length; i++) {
	        		_html+='<li>'
                			+'<img src="'+res.body[i].photoAlbumModelCover+'" class="picId" data-id="'+res.body[i].photoAlbumID+'" alt="#">'
                			+'<em></em>'
                			+'<div class="desc"><p class="total">'+res.body[i].modelName+'</p>'
                			+'<p class="time">'+res.body[i].orderDate.split(" ")[0]+'</p></div>'
            			+'</li>';
            			$(".list-bot").html(_html);
	        	};
	        	$("#loading").hide();
	        },error:function(){
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