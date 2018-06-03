package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.Comment;
import com.wishes.yearOld.model.Reference;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/24
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */

@Component("commentMapper")
public interface CommentMapper {
    void insertComment(Comment comment);

    void insertReference(Reference reference);

    Integer selectCommentId(Comment comment);

    List<Comment> selectCommentList(Comment comment);

    Integer selectTimeLineCommentCount(@Param("commentType")Byte commentType, @Param("id")Integer id, @Param("status")Byte status);

    Comment selectCommentById(@Param("referId")Integer referId,@Param("status")Byte status);

    List<Comment> selectUserComment(@Param("userGroup")Integer userGroup,
                                     @Param("toUserID")Integer toUserID,
                                     @Param("status")Byte status,
                                     @Param("start")Integer start,
                                     @Param("pageSize")Integer pageSize);

    Integer selectUserCommentCount(@Param("userGroup")Integer userGroup,
                                    @Param("toUserID")Integer toUserID,
                                    @Param("status")Byte status,
                                   @Param("lastUpdateTime")Date lastUpdateTime);

}
