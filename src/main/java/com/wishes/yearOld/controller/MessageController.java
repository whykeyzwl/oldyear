package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.dao.PhotoAlbumDao;
import com.wishes.yearOld.dao.SysMessageMapper;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2016/11/8
 * Description:
 */

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TimeLineService timeLineService;

    @Autowired
    private PhotoAlbumService photoAlbumService;

    /**
     * 获取at我的消息列表
     * @param pageNo
     * @param pageSize
     * return referenceMeVO [{reference:{},comment:{},photoAlbum:{},com_timeLineVO:{},ref_timeLineVO:{}},{...},...]
     * reference at我的基本信息
     * comment at来源于评论,评论的信息
     * ref_timeLineVO at来源于个人动态,个人动态信息
     * photoAlbum at来源于评论,评论是图集
     * com_timeLineVO at来源于评论,评论是个人动态
     */
    @RequestMapping(value="/post_refer_list_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getReferList(Integer pageNo, Integer pageSize,HttpServletRequest request) {
        try {
            //Integer userId = 1;
            User user = (User)request.getAttribute("user");
            Integer userId = user.getId();
            //根据userId获取@我的reference列表
            Integer start = (pageNo-1)*pageSize;
            Byte status = Constant.DEFAULT_COMMENT_STATUS;
            List<Reference> referenceList = messageService.selectReferenceByUserId(start,pageSize,userId,status);
            List<ReferenceMeVO> voList = null;
            if(referenceList != null && referenceList.size()>0){
                voList = new ArrayList<>();
                for (Reference reference:referenceList){
                    ReferenceMeVO vo = new ReferenceMeVO();
                    //把@信息--reference 返回
                    vo.setReference(reference);
                    //根据@类型查找具体的@来源 1-评论 2-个人动态
                    int referType = reference.getReferType();
                    int referId = reference.getReferId();//评论的id or 个人动态的id
                    if(Constant.REFER_FROM_COM == referType){//评论
                        //根据评论id去查询对应的评论
                        Comment comment = commentService.selectCommentById(referId,status);
                        if(comment != null){
                            //把评论返回
                            vo.setComment(comment);
                            Byte commentType = comment.getType();
                            Integer commentObjId = comment.getObjId();
                            if(Constant.ALBUM_COMMENT_TYPE == commentType){//图集评论
                                PhotoAlbum photoAlbum = photoAlbumService.detail(commentObjId,null,false);
                                if(photoAlbum != null){
                                    //把图集返回
                                    vo.setPhotoAlbum(photoAlbum);
                                }
                            }else if(Constant.TIMELINE_COMMENT_TYPE == commentType){//个人动态评论
                                TimeLineVO timeLineVO = timeLineService.readTimeLine(commentObjId);
                                if(timeLineVO != null){
                                    Byte content_type = timeLineVO.getContentType();
                                    if(content_type == 2){//动态为作品动态--图集
                                        Integer albumId = timeLineVO.getAlbumId();
                                        if(albumId != null){
                                            timeLineVO.setAlbum(photoAlbumService.detail(albumId,null,false));
                                        }
                                    }else if (content_type == 0){//动态为图文
                                        Integer timelineId = timeLineVO.getId();
                                        List<String> photos = timeLineService.listTimeLinePhoto(timelineId);
                                        if(photos != null && photos.size()>0) {
                                            timeLineVO.setPhotos(photos);
                                        }
                                    }
                                }
                                //把个人动态返回
                                vo.setCom_timeLineVO(timeLineVO);
                            }
                        }
                    }else if(Constant.REFER_FROM_LINE == referType){//个人动态
                        //根据个人动态的id去查询对应的个人动态
                        TimeLineVO timeLineVO = timeLineService.readTimeLine(referId);
                        if(timeLineVO != null){
                            Byte content_type = timeLineVO.getContentType();
                            if(content_type == 2){//动态为作品动态--图集
                                Integer albumId = timeLineVO.getAlbumId();
                                if(albumId != null){
                                    timeLineVO.setAlbum(photoAlbumService.detail(albumId,null,false));
                                }
                            }else if (content_type == 0){//动态为图文
                                Integer timelineId = timeLineVO.getId();
                                List<String> photos = timeLineService.listTimeLinePhoto(timelineId);
                                if(photos != null && photos.size()>0){
                                    timeLineVO.setPhotos(photos);
                                }
                            }
                        }
                        vo.setRef_timeLineVO(timeLineVO);
                    }
                    voList.add(vo);
                }
            }else {
                return Result.BuildFailResult(ResponseCode.SC_OK, "暂无@我的消息");
            }
            return  Result.BuildSuccessResult(voList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器内部错误");
        }
    }

    /**
     * 获取评论我的消息列表
     * @param pageNo
     * @param pageSize
     * return CommentMeVO [{comment:{},photoAlbum:{},timeLineVO:{},fromUser:{}},{...},...]
     * comment  评论基本信息
     * photoAlbum 评论对象为图集
     * timeLineVO   评论对象为个人动态
     * fromUser 发表评论人
     */
    @RequestMapping(value="/post_comment_list_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getCommentList(Integer pageNo,Integer pageSize,HttpServletRequest request) {
        try {
//            Integer userId = 1073069;
//            Integer userGroup = 1;
            User user = (User)request.getAttribute("user");
            Integer userId = user.getId();
            Integer userGroup = user.getGroupId();
            Integer start = (pageNo-1)*pageSize;
            List<Comment> commentList = null;
            List<CommentMeVO> voList = null;//返回的评论信息列表
            //获取评论我的基本消息列表
            commentList = commentService.selectUserComment(userGroup, userId, Constant.DEFAULT_COMMENT_STATUS, start, pageSize);
            if(commentList != null && commentList.size()>0){
                voList = new ArrayList<>();
                for(Comment comment : commentList){
                    CommentMeVO vo = new CommentMeVO();
                    //返回的评论基本信息
                    vo.setComment(comment);
                    //评论类型 '1- 评论图集 2- 评论个人动态'
                    Byte commentType = comment.getType();
                    Integer commentObjId = comment.getObjId();
                    if(Constant.ALBUM_COMMENT_TYPE == commentType){//1- 评论图集
                        PhotoAlbum photoAlbum = photoAlbumService.loadPhotoAlbum(commentObjId);
                        vo.setPhotoAlbum(photoAlbum);
                    }else if(Constant.TIMELINE_COMMENT_TYPE == commentType){//2- 评论个人动态
                        TimeLineVO timeLineVO = timeLineService.readTimeLine(commentObjId);
                        if(timeLineVO != null){
                            Byte content_type = timeLineVO.getContentType();
                            if(content_type == 2){//动态为作品动态--图集
                                Integer albumId = timeLineVO.getAlbumId();
                                if(albumId != null){
                                    timeLineVO.setAlbum(photoAlbumService.loadPhotoAlbum(albumId));
                                }
                            }else if (content_type == 0){//动态为图文
                                Integer timelineId = timeLineVO.getId();
                                List<String> photos = timeLineService.listTimeLinePhoto(timelineId);
                                if(photos != null && photos.size()>0){
                                    timeLineVO.setPhotos(photos);
                                }
                            }
                        }
                        vo.setTimeLineVO(timeLineVO);
                    }
                    Integer fromId = comment.getFromUserId();
                    User fromUser = userService.detail(fromId,null);
                    vo.setFromUser(fromUser);
                    voList.add(vo);
                }
            }else{
                return Result.BuildFailResult(ResponseCode.SC_OK,"暂无评论我的消息");
            }
            return Result.BuildSuccessResult(voList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器内部错误");
        }
    }

    /**
     * 获取系统消息列表
     * @param pageNo
     * @param pageSize
     */
    @RequestMapping(value="/post_sysmsg_list_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getSysMessageList(Integer pageNo,Integer pageSize,HttpServletRequest request) {
        User user = (User)request.getAttribute("user");
        Integer start = (pageNo-1)*pageSize;
        try {
            List<SysMessage> sysMessageList = sysMessageMapper.selectSysMessageList(start, pageSize,user.getId(),user.getGroupId(),new Date());
            if(sysMessageList != null && sysMessageList.size()>0){
                return Result.BuildSuccessResult(sysMessageList);
            }
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "暂无系统消息");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"服务器内部错误");
        }
    }

    /**
     * 定时获取消息更新状况
     * @param lastUpdateTime 最后一次更新时间，手机端维护更新到本地记录的最新时间戳
     */
    @RequestMapping(value="/post_notify_new_message_auth.json",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result notifyNewMessage(Long lastUpdateTime,HttpServletRequest request) {
        Date lastUpdate = null;
        if(lastUpdateTime!=null) {
            lastUpdate = new Date();
            lastUpdate.setTime(lastUpdateTime);
        }
        User user = (User)request.getAttribute("user");

        Integer sysMessageCount = sysMessageMapper.queryNewSysMessageCount(lastUpdate,user.getId(),user.getGroupId(),new Date());

        Integer referenceCount = sysMessageMapper.queryNewReferenceCount(lastUpdate,user.getId());

        Integer commentCount = commentService.selectUserCommentCount(user.getGroupId(),user.getId(),(byte)0,lastUpdate);

        Map<String,Long> result = new HashMap<String,Long>();
        result.put("sysMessageCount",sysMessageCount.longValue());
        result.put("referenceCount",referenceCount.longValue());
        result.put("commentCount",commentCount.longValue());
        result.put("queryUpdateTime",lastUpdateTime);
        return Result.BuildSuccessResult(result);  
    }

    /**
     * 发布新系统消息
     * @param
     */
    @RequestMapping(value="/addSysMessage.action",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result addSysMessage(String title, Integer sendType, String sendTo, String publishTime, String content,HttpServletRequest request) {
        if (StringUtils.isEmpty(title)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "系统消息标题必填");
        }
        if (sendType == 4 && StringUtils.isEmpty(sendTo)) {
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "指定用户id必填");
        }

        Date publishDate = null;
        if (StringUtils.isEmpty(publishTime)) {
            publishDate = new Date();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                publishDate = sdf.parse(publishTime);
            } catch (ParseException e) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "无效的发布时间");
            }
        }
        SysMessage msg = new SysMessage();
        msg.setContent(content);
        msg.setTitle(title);
        msg.setPublishTime(publishDate);
        msg.setStatus((byte) 1);
        switch (sendType) {
            case 0:
                msg.setPublishType(0);
                break;
            case 1:
                msg.setPublishType(1);
                msg.setPublishObj(String.valueOf(Constant.USER_GROUP_MODEL));
                break;
            case 2:
                msg.setPublishType(1);
                msg.setPublishObj(String.valueOf(Constant.USER_GROUP_PHOTOGRAPHER));
                break;
            case 3:
                msg.setPublishType(1);
                msg.setPublishObj(String.valueOf(Constant.USER_GROUP_USER));
                break;
            case 4:
                msg.setPublishType(3);
                //// TODO: 2016/11/30
                msg.setPublishObj(sendTo);
                break;
        }

        try{
            int ret = sysMessageMapper.insertSelective(msg);
            if(ret>0)
                return Result.BuildSuccessResult(null);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR,"添加失败");
    }
}
