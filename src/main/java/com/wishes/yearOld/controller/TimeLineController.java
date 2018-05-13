package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.CommentService;
import com.wishes.yearOld.service.IUserService;
import com.wishes.yearOld.service.PhotoAlbumService;
import com.wishes.yearOld.service.TimeLineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/25
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/timeline")
public class TimeLineController {

    @Autowired
    private PhotoAlbumService photoAlbumService;

    @Autowired
    private TimeLineService timeLineService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private IUserService userService;


    @RequestMapping(value = "/post_add_timeline_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result addTimeLine(Byte type,TimeLine timeLine,HttpServletRequest request){

        User user = (User)request.getAttribute("user");
        if(user == null){
            return Result.BuildFailResult(ResponseCode.SC_UNAUTHORIZED, "请先登录再发表动态");
        }
        timeLine.setUserid(user.getId());
        timeLine.setContentType(type);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strTime = sdf.format(new Date());
        try {
            Date timeLineTime = sdf.parse(strTime);
            timeLine.setCreatedTime(timeLineTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeLine.setStatus(Constant.DEFAULT_COMMENT_STATUS);

        try{
            user = userService.detail(user.getId(),null);
            if(user == null){
                return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
            }
            TimeLineVO vo = new TimeLineVO();
            vo.setUserId(user.getId());
            vo.setNickname(user.getNickName());
            vo.setFace(user.getFaceUrl());
            vo.setContentType(type);
            vo.setCreatedTime(timeLine.getCreatedTime());

            GodViewLikeVO likeVO =  new GodViewLikeVO();
            likeVO.setUserId(user.getId());
            likeVO.setLikeType(Constant.LIKE_TYPE_COMMENT);
            //type, content, albumId,picUrl1...picUrl9

            PhotoAlbum photoAlbum;
            Integer likeCount = 0;
            Integer commentCount = 0;
            //type为0 图文 content为文字 九张图
            if(type==0){
                List<String> picLists = new ArrayList<>();
                if(StringUtils.isNotEmpty(timeLine.getPicUrl1()))
                    picLists.add(timeLine.getPicUrl1());
                if(StringUtils.isNotEmpty(timeLine.getPicUrl2()))
                    picLists.add(timeLine.getPicUrl2());
                if(StringUtils.isNotEmpty(timeLine.getPicUrl3()))
                    picLists.add(timeLine.getPicUrl3());
                if(StringUtils.isNotEmpty(timeLine.getPicUrl4()))
                    picLists.add(timeLine.getPicUrl4());
                if(StringUtils.isNotEmpty(timeLine.getPicUrl5()))
                    picLists.add(timeLine.getPicUrl5());
                if(StringUtils.isNotEmpty(timeLine.getPicUrl6()))
                    picLists.add(timeLine.getPicUrl6());
                if(StringUtils.isNotEmpty(timeLine.getPicUrl7()))
                    picLists.add(timeLine.getPicUrl7());
                if(StringUtils.isNotEmpty(timeLine.getPicUrl8()))
                    picLists.add(timeLine.getPicUrl8());
                if(StringUtils.isNotEmpty(timeLine.getPicUrl9()))
                    picLists.add(timeLine.getPicUrl9());
                vo.setContent(timeLine.getContent());
                vo.setPhotos(picLists);
                Integer id = timeLineService.addTextTimeLine(timeLine, picLists);
                //查询点赞总数
                likeVO.setLikeId(id);
                likeCount = timeLineService.selectTimeLineLikeCount(likeVO);
                //查询评论总数
                commentCount = commentService.selectTimeLineCommentCount(Constant.TIMELINE_COMMENT_TYPE, id, Constant.DEFAULT_COMMENT_STATUS);
            }

            //type为2 作品动态 content为图集标题
            if(type==2){
                photoAlbum = photoAlbumService.loadPhotoAlbum(timeLine.getAlbumid());
                if(photoAlbum != null){
                    timeLine.setContent(photoAlbum.getTitle());
                    Integer id = timeLineService.addAlbumTimeLine(timeLine);
                    likeVO.setLikeId(id);
                    //查询点赞总数
                    likeCount = timeLineService.selectTimeLineLikeCount(likeVO);
                    //查询评论总数
                    commentCount = commentService.selectTimeLineCommentCount(Constant.ALBUM_COMMENT_TYPE, id, Constant.DEFAULT_COMMENT_STATUS);
                }
                vo.setAlbum(photoAlbum);
            }
            vo.setCommentCount(commentCount);
            vo.setLikeCount(likeCount);
            return Result.BuildSuccessResult(vo);
        }catch (Exception e){
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
    }

    /**
     * 获取个人动态列表,默认查询status=0的记录，按照创建时间倒序
     * @param userId 用户ID
     * @param pageNo 页号
     * @param pageSize 页面大小
     */
    @RequestMapping(value = "/list_timeline_by_user.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result listTimeLine(Integer userId,Integer pageNo,Integer pageSize,String passportId){
        Map<String,Integer> param = new HashMap<>();
        param.put("userId",userId);
        param.put("start",(pageNo-1)*pageSize);
        param.put("pageSize",pageSize);
        param.put("status",(int)Constant.DEFAULT_COMMENT_STATUS);
        List<TimeLineVO> lists;
        try{
            lists = timeLineService.listTimeLine(param);
            if(lists == null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "动态列表不存在");
            }
            GodViewLikeVO likeVO =  new GodViewLikeVO();
            likeVO.setLikeType(Constant.LIKE_TYPE_COMMENT);
            for(TimeLineVO vo :lists){
                Integer albumId = vo.getAlbumId();
                Integer timelineId = vo.getId();
                if(albumId>0){
                    vo.setAlbum(photoAlbumService.loadPhotoAlbum(albumId));
                }
                List<String> photos = timeLineService.listTimeLinePhoto(timelineId);
                if(photos != null && photos.size()>0){
                    vo.setPhotos(photos);
                }
                likeVO.setLikeId(timelineId);
                Integer likeCount = timeLineService.selectTimeLineLikeCount(likeVO);
                vo.setLikeCount(likeCount);

                Integer loginUserId = null;
                if(StringUtils.isNotEmpty(passportId)){
                    User user = userService.loadUserFromCache(passportId);
                    if(user!=null) loginUserId = user.getId();
                }
                boolean isLikeByCurUser = userService.isLikedByCurUser(vo.getId(), Constant.LIKE_TYPE_COMMENT, loginUserId);
                vo.setLikedByCurUser(isLikeByCurUser);
                Integer commentCount = timeLineService.selectTimeLineCommentCount(Constant.TIMELINE_COMMENT_TYPE,timelineId,Constant.DEFAULT_COMMENT_STATUS);
                vo.setCommentCount(commentCount);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
        return Result.BuildSuccessResult(lists);
    }

    /**
     * 获取个人动态详情，点击图文信息时进入
     * @param timelineId 个人动态ID
     * @param likeNum 默认取点赞列表的条数,为空默认10条，-1则全取
     * @param commentNum 默认取最新评论列表的条数,为空默认10条，-1则全取
     */
    @RequestMapping(value = "/read_timeline.json",  produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result readTimeLine(Integer timelineId,
                               @RequestParam(required = false)Integer likeNum,
                               @RequestParam(required = false)Integer commentNum,
                               @RequestParam(required = false)String passportId){
        TimeLineVO vo;

        if(timelineId==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "动态id不能为空");
        }
        try{
            vo = timeLineService.readTimeLine(timelineId);
            if(vo==null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "动态已经不存在");
            }
            List<String> photos = timeLineService.listTimeLinePhoto(timelineId);
            if(photos != null && photos.size()>0){
                vo.setPhotos(photos);
            }
            GodViewLikeVO likeVO =  new GodViewLikeVO();
            likeVO.setLikeType(Constant.LIKE_TYPE_COMMENT);
            likeVO.setLikeId(timelineId);
            Integer likeCount = timeLineService.selectTimeLineLikeCount(likeVO);
            vo.setLikeCount(likeCount);
            if(likeNum==null){
                likeNum = 10;
            }
            likeVO.setLikeNum(likeNum);
            List<User> likeUsers = timeLineService.selectLikeUsers(likeVO);
            vo.setLikeUsers(likeUsers);
            Integer commentCount = timeLineService.selectTimeLineCommentCount(Constant.TIMELINE_COMMENT_TYPE,timelineId,Constant.DEFAULT_COMMENT_STATUS);
            vo.setCommentCount(commentCount);
            if(commentNum==null){
                commentNum = 10;
            }
            List<Comment> comments = timeLineService.selectComments(Constant.TIMELINE_COMMENT_TYPE,Constant.DEFAULT_COMMENT_STATUS,timelineId,commentNum);
            vo.setComments(comments);
            Integer loginUserId = null;
            if(StringUtils.isNotEmpty(passportId)){
                User user = userService.loadUserFromCache(passportId);
                if(user!=null) loginUserId = user.getId();
            }
            boolean isLikeByCurUser = userService.isLikedByCurUser(vo.getId(), Constant.LIKE_TYPE_COMMENT, loginUserId);
            vo.setLikedByCurUser(isLikeByCurUser);
            return Result.BuildSuccessResult(vo);
        }catch (Exception e){
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
    }


    /**
     * 给动态点赞/取消点赞
     * @param timelineId 动态id
     */
    @RequestMapping(value = "/post_timeline_like_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result timeLineLike(Integer timelineId,HttpServletRequest request){
        if(timelineId==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "动态id不能为空");
        }
        Integer line = timeLineService.selectById(timelineId);
        if(line == 0){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "当前动态不存在");
        }
        GodViewLikeVO likeVO =  new GodViewLikeVO();
        User user = (User)request.getAttribute("user");
        int userId = user.getId();
        likeVO.setUserId(userId);
        likeVO.setLikeId(timelineId);
        likeVO.setLikeType(Constant.LIKE_TYPE_COMMENT);

        Integer likeCount = timeLineService.selectTimeLineLikeCount(likeVO);

        GodViewLikeVO vo = timeLineService.selectIsLikeTimeLine(likeVO);

        try {
            if (vo != null) {
                //取消点赞
                timeLineService.giveDownTimeLine(vo);
                likeCount--;
            } else {
                //点赞
                likeVO.setCreateTime(new Date());
                timeLineService.giveUpTimeLine(likeVO);
                likeCount++;
            }
            return Result.BuildSuccessResult(likeCount);
        }catch(Exception e){
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
    }


    /**
     * 获取点赞用户列表
     * @param timelineId 动态id
     * @param pageNo 页号
     * @param pageSize 页面大小
     */
    @RequestMapping(value = "/list_like_users.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result listLikeUsers(Integer timelineId,Integer pageNo,Integer pageSize){
        Map<String,Integer> map = new HashMap<>();
        map.put("likeId",timelineId);
        map.put("start",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        try{
            User user = timeLineService.detailLikeUsers(map);
            if(user != null){
                return Result.BuildSuccessResult(user);
            }
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "暂无点赞用户");
        }catch (Exception e){
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器内部错误");
        }
    }

    /**
     * 获取关注用户的个人动态,默认查询status=0的记录，按照创建时间倒序
     * @param pageNo 页号
     * @param pageSize 页面大小
     */
    @RequestMapping(value = "/post_focus_timeline_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result listFocusTimeLine(Integer pageNo,Integer pageSize,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Map<String,Integer> param = new HashMap<>();
        param.put("userId",user.getId());
        param.put("start",(pageNo-1)*pageSize);
        param.put("pageSize",pageSize);
        param.put("status",(int)Constant.DEFAULT_COMMENT_STATUS);
        param.put("focus",1);
        List<TimeLineVO> lists;
        try{
            lists = timeLineService.listTimeLine(param);
            if(lists == null){
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "动态列表不存在");
            }
            GodViewLikeVO likeVO =  new GodViewLikeVO();
            likeVO.setLikeType(Constant.LIKE_TYPE_COMMENT);
            for(TimeLineVO vo :lists){
                Integer albumId = vo.getAlbumId();
                Integer timelineId = vo.getId();
                if(albumId>0){
                    vo.setAlbum(photoAlbumService.loadPhotoAlbum(albumId));
                }
                List<String> photos = timeLineService.listTimeLinePhoto(timelineId);
                if(photos != null && photos.size()>0){
                    vo.setPhotos(photos);
                }
                likeVO.setLikeId(timelineId);
                Integer likeCount = timeLineService.selectTimeLineLikeCount(likeVO);
                vo.setLikeCount(likeCount);
                boolean isLikeByCurUser = userService.isLikedByCurUser(vo.getId(), Constant.LIKE_TYPE_COMMENT, user.getId());
                vo.setLikedByCurUser(isLikeByCurUser);
                Integer commentCount = timeLineService.selectTimeLineCommentCount(Constant.TIMELINE_COMMENT_TYPE,timelineId,Constant.DEFAULT_COMMENT_STATUS);
                vo.setCommentCount(commentCount);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
        return Result.BuildSuccessResult(lists);
    }
}
