package com.wishes.yearOld.controller;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.CommentService;
import com.wishes.yearOld.service.IUserService;
import com.wishes.yearOld.service.PhotoAlbumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/24
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private IUserService userService;

    @Autowired
    private PhotoAlbumService photoAlbumService;


    /**
     * 添加评论
     *
     * param toUserId 被回复用户id，可选，为空时默认评论当前对象
     * param type 评论类型 1- 评论图集 2- 评论个人动态
     * param objId 评论对象，type=1 图集id，type=2 个人动态id
     * param content 评论内容
     * param referUsers 评论中@到的用户id,多个用','连接,可以为空
     * returns 新添加的评论内容
     */
    @RequestMapping(value = "/post_add_comment_auth.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result addComment(Comment comment,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        comment.setFromUserId(user.getId());
        if(comment.getToUserId() == null){
            comment.setToUserId(Constant.DEF_COMMENT_TOID);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strTime = sdf.format(new Date());
        try {
            Date commentTime = sdf.parse(strTime);
            comment.setCommentTime(commentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        comment.setStatus(Constant.DEFAULT_COMMENT_STATUS);
        try{
            commentService.insertComment(comment);
            //查询toUserName
            User toUser = userService.detail(comment.getToUserId(),null);

            //fromUserName,fromFaceUrl
            User fromUser = userService.detail(comment.getFromUserId(),null);

            if(toUser != null){
                comment.setToUserName(toUser.getNickName());
            }
            if(fromUser != null){
                comment.setFromUserName(fromUser.getNickName());
                comment.setFromFaceUrl(fromUser.getFaceUrl());
            }
            return Result.BuildSuccessResult(comment);
        }catch (Exception e){
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
    }


    /**
     * 获取评论列表,默认查询status=0的记录，
     *
     * param type 评论类型 1- 评论图集 2- 评论个人动态
     * param objId 评论对象，type=1 图集id，type=2 个人动态id
     * param pageNo 页号
     * param pageSize 页面大小
     * param desc 可以为空默认按照评论时间正序，=1时按照评论时间倒序
     * returns {*[]}
     */
    @RequestMapping(value = "/list_comment.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result listComment(Comment comment){
        comment.setStatus(Constant.DEFAULT_COMMENT_STATUS);
        List<Comment> commentList = null;
        try {
            commentList = commentService.selectCommentList(comment);
            if (commentList == null) {
                return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "参数错误");
            }
            if (comment.getType() == Constant.ALBUM_COMMENT_TYPE) {
                PhotoAlbum p = photoAlbumService.loadPhotoAlbum(comment.getObjId());
                if(p == null){
                    return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST, "图集不存在");
                }
                for (Comment obj : commentList) {
                    AlbumPermission permission = photoAlbumService.calUserAlbumPermission(p,obj.getFromUserId());
                    if("预售作品".equals(permission.getPermitDesp()))
                        obj.setConsumerType(2);
                    else if("已购买".equals(permission.getPermitDesp()))
                        obj.setConsumerType(1);
                    else
                        obj.setConsumerType(0);
                    //TODO: 用户级别
                }
            }
            return Result.BuildSuccessResult(commentList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.BuildFailResult(ResponseCode.SC_INTERNAL_SERVER_ERROR, "系统异常");
        }
    }
}
