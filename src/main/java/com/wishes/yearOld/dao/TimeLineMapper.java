package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("timeLineMapper")
public interface TimeLineMapper {
    int insertSelective(TimeLine record);

    GodViewLikeVO selectIsLikeTimeLine(GodViewLikeVO likeVO);

    Integer selectTimeLineLikeCount(GodViewLikeVO likeVO);

    void giveDownTimeLine(GodViewLikeVO vo);

    void giveUpTimeLine(GodViewLikeVO likeVO);

    User detailLikeUsers(Map<String, Integer> map);

    List<TimeLineVO> listTimeLine(Map<String, Integer> param);

    List<String> listTimeLinePhoto(Integer timelineId);

    Integer selectTimeLineCommentCount(@Param("commentType")byte commentType, @Param("commentObjId")Integer commentObjId, @Param("status")byte status);

    TimeLineVO readTimeLine(Integer timelineId);

    List<User> selectLikeUsers(GodViewLikeVO likeVO);

    List<Comment> selectComments(@Param("type")Byte type,@Param("status")Byte status,@Param("timelineId")Integer timelineId, @Param("commentNum")Integer commentNum);

    Integer selectById(Integer timelineId);
}