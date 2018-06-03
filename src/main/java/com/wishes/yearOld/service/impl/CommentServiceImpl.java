package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.dao.CommentMapper;
import com.wishes.yearOld.model.Comment;
import com.wishes.yearOld.model.Reference;
import com.wishes.yearOld.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/24
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void insertComment(Comment comment) {

        commentMapper.insertComment(comment);

        Integer[] referUserIds = comment.getReferUserIds();

        if(referUserIds != null){
            Integer commentId = commentMapper.selectCommentId(comment);
            Reference reference = new Reference();
            reference.setReferId(commentId);
            reference.setReferType(Constant.REFER_FROM_COM);
            reference.setContent(comment.getContent());
            reference.setCommentTime(comment.getCommentTime());
            reference.setStatus(comment.getStatus());
            for(Integer referUserId : referUserIds){
                reference.setReferUserId(referUserId);
                commentMapper.insertReference(reference);
            }
        }

    }

    @Override
    public List<Comment> selectCommentList(Comment comment) {
        return commentMapper.selectCommentList(comment);
    }

    @Override
    public Integer selectTimeLineCommentCount(Byte commentType, Integer id, Byte status) {
        return commentMapper.selectTimeLineCommentCount(commentType, id, status);
    }

    @Override
    public Comment selectCommentById(Integer referId, Byte status) {
        return commentMapper.selectCommentById(referId,status);
    }

    @Override
    public List<Comment> selectUserComment(Integer userGroup,Integer toUserID,Byte status,Integer start,Integer pageSize) {
        return commentMapper.selectUserComment(userGroup,toUserID, status, start, pageSize);
    }

    @Override
    public Integer selectUserCommentCount(Integer userGroup,Integer toUserID,Byte status,Date lastUpdateTime){
        return commentMapper.selectUserCommentCount(userGroup,toUserID,status,lastUpdateTime);
    }
}
