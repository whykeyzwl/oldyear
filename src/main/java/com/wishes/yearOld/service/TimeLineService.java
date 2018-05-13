package com.wishes.yearOld.service;

import com.wishes.yearOld.model.*;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/25
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public interface TimeLineService {
    Integer addTextTimeLine(TimeLine timeLine,List<String> picLists);

    Integer addAlbumTimeLine(TimeLine timeLine);

    GodViewLikeVO selectIsLikeTimeLine(GodViewLikeVO likeVO);

    Integer selectTimeLineLikeCount(GodViewLikeVO likeVO);

    void giveDownTimeLine(GodViewLikeVO vo);

    void giveUpTimeLine(GodViewLikeVO likeVO);

    User detailLikeUsers(Map<String, Integer> map);

    List<TimeLineVO> listTimeLine(Map<String, Integer> param);

    List<String> listTimeLinePhoto(Integer timelineId);

    Integer selectTimeLineCommentCount(byte commentType, Integer commentObjId, byte status);

    TimeLineVO readTimeLine(Integer timelineId);

    List<User> selectLikeUsers(GodViewLikeVO likeVO);

    List<Comment> selectComments(Byte type,Byte status,Integer timelineId, Integer commentNum);

    Integer selectById(Integer timelineId);
}
