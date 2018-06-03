var _hothtml = "";
var _newhtml = "";
// 域名
var url = "https://appapi.qingdouke.com";
// 参数
var pageNo = 1;
var pageSize = 12;
var typeLevel = 3;
// 第三方id
var _passportId = "";
// 图集id
var picId = "";
var threelogin = localStorage.getItem("threelogin");
if(threelogin == "sina" || threelogin == "qq"){
    _passportId = window.location.href.split("?")[1].split("=")[1];
    localStorage.setItem("token",_passportId);
} 

// 获取第三方用户信息
threeLogin();
function threeLogin(){
    $.ajax({
        url : url + "/wap/getUser.json",
        type:"GET",
        data:{
            passportId : _passportId
        },
        success:function(data){
            console.log(data);
            localStorage.setItem("faceUrl", data.faceUrl);
            localStorage.setItem("nickName", data.nickName);
        },
    })
}
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
            if(data.body.length>0){
                  for(i=0;i<data.body.length;i++){
                    _newhtml+='<li>'
                           +'<img src="'+data.body[i].cover+'" alt="'+data.body[i].modelName+'" data-id="'+data.body[i].id+'" class="picId">'
                           +'<em></em><p class="time">'+data.body[i].lastUpdateTime.split(" ")[0]+'</p>'
                           +'<p class="total"><i></i>'+data.body[i].viewCount+'</p>'
                           +'</li>';
                    $(".newView").html(_newhtml);
                    $("#loading").hide();
                }  
            }
            
        },
        error:function(){
            $("#loading").hide();
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
            console.log(data);
            for(i=0;i<data.body.length;i++){
                _hothtml+='<li>'
                       +'<img src="'+data.body[i].cover+'" alt="'+data.body[i].modelName+'" data-id="'+data.body[i].id+'" class="picId">'
                       +'<em></em><div class="desc"><p class="time">'+data.body[i].title+'</p>'
                       +'<p class="total"><i></i>'+data.body[i].viewCount+'</p></div>'
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
// 上拉加载更多
$(window).scroll(function(e){
    scH=$(this).scrollTop();
    winH=$(this).height();
    domH=$(document).height();
    if(scH+winH>=domH){
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
        console.log((endX - startX) + '，' + (endY - startY));
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

// 绑定事件
(function bindChange(){
    // 点击“最热”
    $(".hoter").click(function(){
        console.log("最热")
        $(".refresh").css({"display":"none"});
        _newhtml = ""
        hotgetmore()
        $(".hotView").show().siblings().hide();
    })
    // 点击“最新”
    $(".newer").click(function(){
        $(".refresh").css({"display":"none"});
        console.log("最新")
        $(".newView").show().siblings().hide();
        newgetmore();
        
    })
    // 点击图片跳转详情页
    $(".picId").live("click",function(){
        picId = $(this).attr("data-id");
        console.log(picId);
        location.href = "view/qdk-xiangqing.html?picId="+picId
    })
    // 点击引导页
    $(".onload1 img").click(function(){
        console.log(1)
        $(this).fadeOut();
        $(".onload2").fadeIn();
    })
    $(".onload2 img").click(function(){
        $(this).fadeOut();
    })
})()
















