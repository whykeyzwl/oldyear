package com.wishes.yearOld.service;

import com.wishes.yearOld.model.Comment;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/24
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */
public interface CommentService {

    void insertComment(Comment comment);

    List<Comment> selectCommentList(Comment comment);

    Integer selectTimeLineCommentCount(Byte commentType, Integer id, Byte status);

    Comment selectCommentById(Integer referId,Byte status);

    List<Comment> selectUserComment(Integer userGroup,Integer toUserID,Byte status,Integer start,Integer pageSize);

    Integer selectUserCommentCount(Integer userGroup,Integer toUserID,Byte status,Date lastUpdateTime);


}
