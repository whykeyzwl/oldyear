package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.IUserService;
import com.wishes.yearOld.service.OrderService;
import com.wishes.yearOld.service.PhotoAlbumService;
import com.wishes.yearOld.utils.CrossUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tmg-yesky on 2016/9/19.
 */
@Controller
@RequestMapping("/photoalbum")
public class PhotoAlbumController {

    private static final Logger logger = Logger.getLogger(PhotoAlbumController.class);

    @Autowired
    PhotoAlbumService photoAlbumService;

    @Autowired
    IUserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * 获取写真集列表
     * @param keyword 搜索关键词，为空则搜索搜索全部
     * @param modelId 搜索指定模特的写真
     * @param sortType 排序类型（1：推荐 2：最新 3：销量 4：限免）
     * @param pageNo 页号
     * @param pageSize 每页大小，默认10
     * @return
     */
    @RequestMapping("/search.json")
    @ResponseBody
    public Result search(String passportId ,@RequestParam(value = "keyword", required = false)String keyword, @RequestParam(value = "modelId", required = false)Integer modelId, @RequestParam(value = "sortType", required = false)Integer sortType, @RequestParam(value = "pageNo", required = false)Integer pageNo, @RequestParam(value = "pageSize", required = false)Integer pageSize){
        User user = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            user = userService.loadUserFromCache(passportId);
        }
        List<PhotoAlbum> list = photoAlbumService.search(keyword, modelId, sortType, pageNo, pageSize,user);
        if(list == null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "未搜索到数据");
        }
        return Result.BuildSuccessResult(list);
    }

    /**
     * 获取套图详情
     * @param albumId
     * @return
     */
    @RequestMapping("/detail.json")
    @ResponseBody
    public Result detail(@RequestParam(value = "albumId")Integer albumId, String passportId) {
        if(albumId == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "请输入套图ID");
        }

        User user = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            user = userService.loadUserFromCache(passportId);
        }

        PhotoAlbum a = photoAlbumService.detail(albumId, user, true);
        if(a==null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "访问的套图不存在");
        }
        //当前star
        a.setStar(photoAlbumService.queryAlbumStar(albumId));
        //获取登录用户是否评价该图集
        UserAlbumLog userAlbumLog = new UserAlbumLog();
        if (user != null) {
            if (photoAlbumService.queryAlbumStarByUserId(albumId, user.getId()) > 0)
                a.setCommentByCurUser(true);
             userAlbumLog = photoAlbumService.queryUserAlbumLog(albumId, user.getId());

        }
        Map<String, Object> map = new HashMap<>();
        // 是否被当前登陆用户点赞
        Integer loginUserId = null;
        if(StringUtils.isNotEmpty(passportId)){
            User loginUser = userService.loadUserFromCache(passportId);
            if(loginUser!=null) loginUserId = loginUser.getId();
        }
        boolean isLikeByCurUser = userService.isLikedByCurUser(a.getId(), Constant.LIKE_TYPE_ALBUM, loginUserId);
        a.setLikedByCurUser(isLikeByCurUser);
        boolean focusModelByCurUser = userService.selectIsFocus(a.getModelId(), Constant.LIKE_TYPE_ALBUM, loginUserId);
        boolean focusPhotographerByCurUser = userService.selectIsFocus(a.getPhotoGrapherId(), Constant.LIKE_TYPE_ALBUM, loginUserId);
        a.setFocusModelByCurUser(focusModelByCurUser);
        a.setFocusPhotographerByCurUser(focusPhotographerByCurUser);
        a.setUserAlbumLog(userAlbumLog);
        return Result.BuildSuccessResult(a);
    }

    /**
     * 获取图片详情，如果用户没有该图片的浏览权限，则返回空.
     * @param albumId 相册id
     * @param page 页码
     * @param passportId 非必填，如果已登陆需要传入此参数
     * @return
     */
    @RequestMapping(value = "/post_photoinfo.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result photoinfo(Integer albumId, Integer page, String passportId) {
        if (albumId == null || page == null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "图集id和图片页码不能为空");
        }
        if (page > Constant.PHOTO_PAGE_FREE) {
            //TODO 验证登录以及用户是否有权限
        }
        // 为了保证page参数从1开始，做特殊处理
        Photo p = photoAlbumService.photoinfo(albumId, page - 1);
        if (p == null) {
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "访问的图片不存在");
        }
        return Result.BuildSuccessResult(p);
    }

    /**
     * 返回最近浏览的图集
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value="post_lastest_view_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result lastestView(HttpServletRequest req, Integer pageNo, Integer pageSize){
        User user = (User)req.getAttribute("user");
        int userId = user.getId();

        List<List<AlbumViewLog>> list = photoAlbumService.lastestView(userId, pageNo, pageSize);
        if(list==null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"无返回记录");
        }
        return Result.BuildSuccessResult(list);
    }

    /*
     * 增加图集点击量
     * @param albumId 图集id
     * @param passportId 如果已登陆需要传入此参数
     */
  /*  @RequestMapping(value="/post_add_view_count.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result viewCount(Integer albumId, String passportId){
        if(albumId == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"图集Id不能为空");
        }

        PhotoAlbum album = photoAlbumService.detail(albumId,null,false);
        if(album==null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"无对应图集");
        }
        try{
            if(StringUtils.isEmpty(passportId))
                photoAlbumService.updateViewCount(album,null);
            else{
                User user = userService.loadUserFromCache(passportId);
                photoAlbumService.updateViewCount(album,user);
            }
            return Result.BuildSuccessResult(viewCount);
        }catch (Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    }*/

    /**
     * 查询图集点击量
     * @param albumId 图集id
     */
    @RequestMapping("/get_view_count.json")
    @ResponseBody
    public Result getViewCount(Integer albumId){
        if(albumId == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"图集Id不能为空");
        }
        try{
            Integer viewCount = photoAlbumService.selectViewCount(albumId);
            if(viewCount==null){
                return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"图集不存在");
            }
            return Result.BuildSuccessResult(viewCount);
        }catch (Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    }

    /**
     * 用户图集点赞/取消点赞
     * @param albumId 图集id
     */
    @RequestMapping(value="/post_album_like_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result viewLike(Integer albumId,HttpServletRequest request){
        if(albumId==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"图集Id不能为空");
        }
        Integer likeCount = photoAlbumService.selectAlbumLikeCount(albumId);
        if(likeCount==null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"图集不存在");
        }

        GodViewLikeVO likeVO =  new GodViewLikeVO();
        User user = (User)request.getAttribute("user");
        int userId = user.getId();
        likeVO.setUserId(userId);
        likeVO.setLikeId(albumId);
        likeVO.setLikeType(Constant.LIKE_TYPE_ALBUM);

        //查询是否已赞
        GodViewLikeVO vo = photoAlbumService.selectIsLikeAlbum(likeVO);
        try{
            if(vo != null){
                //取消点赞
                photoAlbumService.giveDownAlbum(vo);
                likeCount--;
                return Result.BuildSuccessResult(likeCount);
            }else{
                //点赞
                likeVO.setCreateTime(new Date());
                photoAlbumService.giveUpAlbum(likeVO);
                likeCount++;
                return Result.BuildSuccessResult(likeCount);
            }
        }catch(Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    }

    /**
     * 获取用户印象选项
     * @param pageNo
     * @param pageSize
     */
    @RequestMapping("/get_random_tags.json")
    @ResponseBody
    public Result getRandomTags(Integer pageNo, Integer pageSize){
        if(pageNo==null){
            pageNo = 1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        try{
            List<Tag> tags = photoAlbumService.randomTags(pageNo,pageSize);
            return Result.BuildSuccessResult(tags);
        }catch (Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    }

    /**
     * 用户提交用户印象
     * @param albumId 图集id
     * @param tags 印象id拼成的字符串，以逗号分割，例如3,5,7
     */
    @RequestMapping(value="/post_submit_tag_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result submitTags(Integer albumId,String tags,Integer starCount, String userComment,HttpServletRequest request){
        try{
            if(albumId==null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"图集Id不能为空");
            }
            PhotoAlbum album = photoAlbumService.findById(albumId);
            if(album==null){
                return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"图集不存在");
            }

            if(starCount==null) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "提交评分不能为空");
            }

            User user = (User)request.getAttribute("user");
            int ret = photoAlbumService.saveUserAlbumLog(user.getId(),albumId,tags,starCount,userComment);
            return Result.BuildSuccessResult("提交"+ret+"个用户印象");
        }catch(Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    }

    /**
     * 青豆兑换一级解锁套图接口（一级解锁已废除）
     * 青豆购买图集
     * @param albumId 套图ID
     * @returns
     */
    @RequestMapping(value="/post_buy_by_qingdou_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result qingdouPayForAlbum(Integer albumId,HttpServletRequest request) {
        try{
            if(albumId==null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"图集Id不能为空");
            }
            PhotoAlbum album = photoAlbumService.findById(albumId);
            if(album==null){
                return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"图集不存在");
            }
            if(album.getQingdou()<=0){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"图集不支持青豆支付");
            }

            User user = (User)request.getAttribute("user");
            Integer userId = user.getId();
            User user_detail = userService.detail(userId,null);
            if(user_detail.getQingdou()<album.getQingdou()){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"用户青豆数量不足，无法购买图集");
            }
            photoAlbumService.updateQDPayForAlbum(user_detail,album);
            return Result.BuildSuccessResult("青豆支付成功");
        }catch(Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    }

    /**
     * 旅拍权限兑换套图接口
     *
     * @param albumId 套图ID
     * @return
     */
    @RequestMapping(value="/post_buy_by_downlimit_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result downLimitPayForAlbum(Integer albumId,HttpServletRequest request) {
        try{
            if(albumId==null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"图集Id不能为空");
            }
            PhotoAlbum album = photoAlbumService.findById(albumId);
            if(album==null){
                return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"图集不存在");
            }
            if(album.getActivityId()==null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"非活动图集不支持预售权限下载");
            }

            User user = (User)request.getAttribute("user");
            int ret = photoAlbumService.updateDownLimitForAlbum(user,album);
            if(ret==-1){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"用户无预售权限解锁图集");
            }

            return Result.BuildSuccessResult("使用权限解锁成功");
        }catch(Exception e){
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器异常");
        }
    }


    /**
     * 获取套图评价
     *
     * @param albumId
     * @return
     */
    @RequestMapping(value = "/post_subtract_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result subtract(@RequestParam(value = "albumId") Integer albumId, HttpServletRequest request) {
        if (albumId == null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "请输入套图ID");
        }
        Map<String, Object> map = new HashMap<>();
        User user = (User) request.getAttribute("user");
        PhotoAlbum a = photoAlbumService.findById(albumId);
        if (a == null) {
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "访问的套图不存在");
        }
        User model = userService.detail(a.getModelId(), user.getId());
        a.setModelFace(model.getFaceUrl());
        a.setFocusModelByCurUser(model.isFocusByCurUser());
        a.setModelAlbums(model.getAlbums());
        a.setModelName(model.getNickName());

        User photographer = userService.detail(a.getPhotoGrapherId(), user.getId());
        a.setPhotoGrapherFace(photographer.getFaceUrl());
        a.setPhotoGrapherAlbums(photographer.getAlbums());
        a.setFocusPhotographerByCurUser(photographer.isFocusByCurUser());
        a.setPhotoGrapherName(photographer.getNickName());
        a.setStar(photoAlbumService.queryAlbumStar(albumId));

        PhotoAlbum p = new PhotoAlbum();
        p.setLimitFree(0);
        p.setRecommend((byte) 0);
        p.setSortField("publishTime");
        List<PhotoAlbum> noRecommendAlbums = photoAlbumService.list(p);
        if (noRecommendAlbums == null) {
            p.setRecommend((byte) 1);
            p.setSortField("recommendTime");
            noRecommendAlbums = photoAlbumService.list(p);
        }
        Iterator<PhotoAlbum> it = noRecommendAlbums.iterator();
        while (it.hasNext()) {
            PhotoAlbum a1 = it.next();
            if (albumId.equals(a1.getId())) {
                it.remove();
                break;
            }
        }
        //获取登录用户是否评价该图集
        boolean flag = false;
        if(user != null){
            if(photoAlbumService.queryAlbumStarByUserId(albumId,user.getId())>0) flag=true;
        }
        map.put("noRecommendAlbums", noRecommendAlbums);
        map.put("album",a);
        map.put("flag",flag);
        return Result.BuildSuccessResult(map);
    }

    @RequestMapping(value = "/post_albumDitail.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result queryAlbumByPrimaryId(HttpServletRequest req,HttpServletResponse resp,Integer albumId){
    	
        String passportId = req.getParameter("passportId");
        User user = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            user = userService.loadUserFromCache(passportId);
        }
        Integer _have = null;
        if(user != null){
            _have  = orderService.selectHaveBuy(user.getId());
        }
        PhotoAlbum photo = null;
        try {
            photo = photoAlbumService.queryAlbumByPrimaryId(albumId,user,true);
        } catch (Exception e) {
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"系统内部错误，请稍后再试");
        }
        if(photo != null && _have != null){
            if(_have > 0){
                photo.setUserHavebuy(1);
            }else {
                photo.setUserHavebuy(0);
                photo.setPrice3(photo.getViewprice()); 
            }
        }
        System.out.println(""+photo.getPermission().getPermitType());
        CrossUtil.SetHttpServletResponse(resp);
        return Result.BuildSuccessResult(photo);
    }



    /**
     * 用户图集收藏/取消收藏
     *
     * @param albumId 图集id
     */
    @RequestMapping(value = "/home_album_like_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result viewCollection(Integer albumId, HttpServletRequest request) {
        if (albumId == null) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "图集Id不能为空");
        }
        Integer likeCount = photoAlbumService.selectAlbumLikeCount(albumId);
        if (likeCount == null) {
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "图集不存在");
        }

        GodViewLikeVO likeVO = new GodViewLikeVO();
        User user = (User) request.getAttribute("user");
        int userId = user.getId();
        likeVO.setUserId(userId);
        likeVO.setLikeId(albumId);
        likeVO.setLikeType(Constant.LIKE_TYPE_ALBUM);

        //查询是否收藏
        GodViewLikeVO vo = photoAlbumService.selectIsCollectionAlbum(likeVO);
        try {
            if (vo != null) {
                //取消收藏
                photoAlbumService.collectionDownAlbum(vo);
                return Result.BuildSuccessResult("已取消收藏");
            } else {
                //收藏
                likeVO.setCreateTime(new Date());
                photoAlbumService.collectionUpAlbum(likeVO);
                return Result.BuildSuccessResult("已收藏");
            }
        } catch (Exception e) {
            logger.info(e);
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "服务器异常");
        }
    }

    //获取随机推荐图集
    @RequestMapping(value = "/post_album_rand.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getHomePullDown(String passportId, PhotoAlbum photoAlbum){
        if (StringUtils.isNotEmpty(passportId)) {
            User user = userService.loadUserFromCache(passportId);
            photoAlbum.setUserId(user.getId());
        }
        PhotoAlbum _photo = photoAlbumService.getHomeAlbumsRand(photoAlbum);
        if (_photo == null) {
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "你的访问记录不存在");
        }
        return Result.BuildSuccessResult(_photo);
    }
}
