/*rem布局*/
(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            if(clientWidth>=750){
                docEl.style.fontSize='100px';
            }else{
                docEl.style.fontSize=100*(clientWidth/750)+'px';
            }
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);
$(function(){
/*找回密码修改成功*/
    /*$(".next").click(function(){
     $(this).parent(".zhuce").next(".bg").show();
        $(this).parent(".zhuce").siblings(".tan").show();
    });*/
    wid=$(window).width();
    hei=$(window).height();
    //console.log(wid+"-"+hei);
    $(".bgimg").width(wid);
    $(".bgimg").height(hei);
    /*首页选项卡*/
    $(".list-top li").click(function(){
        ind=$(this).index();
        $(this).addClass("on").siblings("li").removeClass("on");
        $(".list-bot").eq(ind).show().siblings(".list-bot").hide();
    });

    /*返回顶部*/
    $(window).scroll(function(){
        scrollH=$(this).scrollTop();
        if(scrollH>=800){
            $(".fix").show();
        }
        else{
            $(".fix").hide();
        }
    });
    $(".fix").click(function(){
        $("html,body").animate({"scrollTop":0},1000);
    });
    /*个人中心浮层*/
    $(".tuichu").click(function(){
        $(this).parents(".grzx").siblings(".bg").show();
        $(this).parents(".grzx").siblings(".qh-fuceng").show();
        $(this).parents(".grzx").siblings(".grzxlist").hide();
    });
    $(".quxiao").click(function(e){
        $(this).parents(".qh-fuceng").hide();
        $(this).parents(".qh-fuceng").siblings(".bg").hide();
        $(this).parents(".qh-fuceng").siblings(".grzxlist").show();
        e.stopPropagation()
    });
    /*详情页轮播图*/
    _mask=$(".mask").html();
    var currentNum = "";
    var _albumId = window.location.href.split("?")[1].split("=")[1];
    var _passportId =  localStorage.getItem("token")?localStorage.getItem("token"):"";
    var imgH=$(".swiper-container").height();
    var imgW=$(".swiper-container").width();
    var maskH=$(".mask").height();
    var maskW=$(".mask").width();
    console.log(imgH+"-"+imgW);
    console.log(maskH+"-"+maskW);
    var _left=(imgW-maskW)/2+"px";
    var _top=(imgH-maskH)/2+"px";
    //$(".swiper-container").height(imgH);
    getIsbuy();
    var swiper = new Swiper('.swiper-container', {
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        observer:true,
        observeParents:true,
        onSlideChangeStart: function(swiper){
            console.log(currentNum);
            if(swiper.activeIndex>=4 && currentNum == 0){
                $(".mask").show();
                $(".mask").css({"top":220,"left":87});
                $(".swiper-slide").css({"-webkit-filter":"blur(7px)"})
            }else{
                $(".mask").hide();
                $(".swiper-slide").css({"-webkit-filter":"blur(0px)"})
            }
        },
        onSlideChangeEnd: function(swiper){
            if(swiper.activeIndex>=4 && currentNum == 0){
                $(".mask").show();
                $(".mask").css({"top":220,"left":87});
                $(".swiper-slide").css({"-webkit-filter":"blur(7px)"})
            }else{
                $(".mask").hide();
                $(".swiper-slide").css({"-webkit-filter":"blur(0px)"})
            }
        },
        onTouchStart: function(swiper,even){
            var imgH=$(".swiper-slide-next img").height();
            var imgW=$(".swiper-slide-next img").width();
            if(imgW>imgH){
                imgmarginT=($(".swiper-slide").height()-$(".swiper-slide-next img").height())/2;
                $(".swiper-slide-next img").css("margin-top",imgmarginT-51)
            }
        }
    });
    // 调用图集接口
    function getIsbuy(){
        $.ajax({
            url : "https://appapi.qingdouke.com/photoalbum/post_albumDitail.json",
            type:"POST",
            data:{
                albumId : _albumId,
                passportId : _passportId
            },
            dataType:"json",
            success:function(data){
                currentNum = data.body.userHavebuy;
            }
        })
    };
    //置顶
    var offH=$(".list-bot").offset().top;
    var offH1 =$("#list-top").offset().top;
    $(window).scroll(function(){
        var scrolltop=$(document).scrollTop();
        console.log(scrolltop);
        console.log(offH);
       if(scrolltop>1250){
            $("#list-top").css({"position": "fixed", "top": "1.15rem", "left": 0, "z-index": 100, "background": "#fff", "padding": "0 0.2rem"});
            $("body").css({"padding-top":"1.99rem"})
            $("#load").css({"position":"fixed","top":0,"left":0,"z-index":100,"background":"#fff"});
        }else {
            $("body").css({"padding-top":"0"})
           $("#load").css({"position": "static"});
           $("#list-top").css({"position": "static"});
        }
    });
    $(window).scroll(function() {
        var scrolltop = $(document).scrollTop();
        if (scrolltop > offH1) {
            $("#load").css({"position": "fixed", "top": 0, "left": 0, "z-index": 100, "background": "#fff"});
        }
        else {
            $("#list-top").css({"position": "static"});
        }
    });
});