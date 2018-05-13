var _hothtml = "";
var _newhtml = "";
// 域名
var url = "http://119.84.79.40:8989";
// 参数
var pageNo = 1;
var pageSize = 8;
var typeLevel = 2;
// 第三方id
var _passportId = "";
// 图集id
var picId = "";
var picType = 1;
// 存储网页链接
var localUrl = "";
localUrl = localStorage.setItem("localUrl", location.href);
console.log(location.href);
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
    };
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
                               +'<em></em><div class="desc"><p class="time">'+data.body[i].title+'</p>'
                               +'<p class="total"><i></i>'+data.body[i].viewCount+'</p></div>'
                               +'</li>';
                    }
                    $(".newView").html(_newhtml);
                    $("#loading").show();
                }
            },
            error:function(){
                $("#loading").hide();
            }
        })
    };
    // 调用最热图集接口
    function hotgetmore(){
        // 调用接口获取数据
        $.ajax({
            url:url+"/index/get_home_albumHot.json?pageNo="+pageNo+"&pageSize=8&typeLevel="+typeLevel,
            type:"POST",
            dataType:"json",
            success:function(data){
                console.log(data);
                   for(i=0;i<data.body.length;i++){
                        _hothtml+='<li>'
                               +'<img src="'+data.body[i].cover+'" alt="'+data.body[i].modelName+'" data-id="'+data.body[i].id+'" class="picId">'
                               +'<em></em><div class="desc"><p class="time">'+data.body[i].title+'</p>'
                               +'<p class="total"><i></i>'+data.body[i].viewCount+'</p></div>'
                               +'</li>';
                    } 
                    $(".hotView").html(_hothtml);
            },
            error:function(){
                $("#loading").hide();
            }
        })
    };
    newgetmore();
    // 上拉加载更多
    $(window).scroll(function(e){
        scH=$(this).scrollTop();
        winH=$(this).height();
        domH=$(document).height();
        console.log(scH+","+winH+","+domH);
        if(scH+winH>=domH){
            pageNo+=1;
            hotgetmore();
            newgetmore();
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
        },false);
        //touchmove事件
        window.addEventListener('touchmove',function(event){
            var touch = event.targetTouches[0];
            endX = touch.pageX;
            endY = touch.pageY;
        },false);
        //touchend事件
        window.addEventListener('touchend',function(event){
            if(endY - startY>135){
                $(".refresh").css({"display":"block"});
                setTimeout(function(){
                    $(".refresh").css({"display":"none"});
                },1000)
                console.log("刷新了");
                pageNo = 1;
                pageSize = 8;
                _newhtml = "";
                _hothtml = "";
                newgetmore();
                hotgetmore();
                
            }
        },false); 
    };

    // 绑定事件
    bindChange();
    function bindChange(){
        // 点击“最热”
        $(".hoter").click(function(){
            picType == 1;
            console.log("最热");
            $(".refresh").css({"display":"none"});
            pageNo = 1;
            _newhtml == "";
            _hothtml == "";
            hotgetmore();
            $(".hotView").show().siblings().hide();
        })
        // 点击“最新”
        $(".newer").click(function(){
            picType == 2;
            pageNo = 1;
            $(".refresh").css({"display":"none"});
            console.log("最新");
            $(".newView").show().siblings().hide();
            _hothtml == "";
            _newhtml == "";
            newgetmore();
        })
        // 点击图片跳转详情页
        $(".picId").live("click",function(){
            picId = $(this).attr("data-id");
            console.log(picId);
            location.href = "view/qdk-xiangqing.html?picId="+picId+"&time="+new Date().getTime();
        })
        /*$(document).live("touchend","img.picId",function(){
            picId = $(this).attr("data-id");
            console.log(picId);
            location.href = "view/qdk-xiangqing.html?picId="+picId
        })*/
        // 点击引导页
        $(".onload1 img").click(function(){
            console.log(1);
            $(this).fadeOut();
            $(".onload2").fadeIn();
        })
        $(".onload2 img").click(function(){
            $(this).fadeOut();
        })
    }
    // 获取第三方用户信息
    var threelogin = localStorage.getItem("threelogin")?localStorage.getItem("threelogin"):"";
    console.log(threelogin);
    if(window.location.href.indexOf("?")>0){
        if(threelogin == "sina" || threelogin == "qq"){
            _passportId = window.location.href.split("?")[1].split("=")[1];
            localStorage.setItem("token",_passportId);
            threeLogin();
        }
    };
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
    };
}















