package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.dao.IUserMapper;
import com.wishes.yearOld.dao.TimeLineMapper;
import com.wishes.yearOld.dao.TimeLinePhotoMapper;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.TimeLineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* Created by IntelliJ IDEA.
* User: zouyu
* Date: 2016/10/25
* Time: 16:07
* To change this template use File | Settings | File Templates.
*/
@Service
public class TimeLineServiceImpl implements TimeLineService {

    @Autowired
    private TimeLineMapper timeLineMapper;

    @Autowired
    private TimeLinePhotoMapper timeLinePhotoMapper;

    @Autowired
    private IUserMapper userMapper;

    @Override
    public Integer addTextTimeLine(TimeLine timeLine,List<String> picLists) {
        int timelineid = timeLineMapper.insertSelective(timeLine);
        TimeLinePhoto timeLinePhoto = new TimeLinePhoto();
//        `id` int(11) NOT NULL AUTO_INCREMENT,
//        `timelineId` int(11) NOT NULL,
//        `imageUrl` varchar(255) NOT NULL,
//        `imageNo` int(11) NOT NULL COMMENT '九宫格的排序 1-9',
//        `createTime` datetime NOT NULL,
//        `status` tinyint(4) NOT NULL COMMENT '和个人动态的status字段同步',
        timeLinePhoto.setTimelineid(timelineid);
        timeLinePhoto.setCreatetime(timeLine.getCreatedTime());
        timeLinePhoto.setStatus(timeLine.getStatus());
        for(int i = 0;i < picLists.size();i ++){
            timeLinePhoto.setImageurl(picLists.get(i));
            timeLinePhoto.setImageno(i+1);
            timeLinePhotoMapper.insertSelective(timeLinePhoto);
        }
        return timelineid;
    }

    @Override
    public Integer addAlbumTimeLine(TimeLine timeLine) {
        timeLineMapper.insertSelective(timeLine);
        userMapper.updateTimeline(timeLine.getUserid());
        return 1;
    }

    @Override
    public GodViewLikeVO selectIsLikeTimeLine(GodViewLikeVO likeVO) {
        return timeLineMapper.selectIsLikeTimeLine(likeVO);
    }

    @Override
    public Integer selectTimeLineLikeCount(GodViewLikeVO likeVO) {
        return timeLineMapper.selectTimeLineLikeCount(likeVO);
    }

    @Override
    public void giveDownTimeLine(GodViewLikeVO likeVO) {
        timeLineMapper.giveDownTimeLine(likeVO);
    }

    @Override
    public void giveUpTimeLine(GodViewLikeVO likeVO) {
        timeLineMapper.giveUpTimeLine(likeVO);
    }

    @Override
    public User detailLikeUsers(Map<String, Integer> map) {
        return timeLineMapper.detailLikeUsers(map);
    }

    @Override
    public List<TimeLineVO> listTimeLine(Map<String, Integer> param) {
        return timeLineMapper.listTimeLine(param);
    }

    @Override
    public List<String> listTimeLinePhoto(Integer timelineId) {
        List<String> urls = timeLineMapper.listTimeLinePhoto(timelineId);
        List<String> photos = null;
        if(urls!=null && urls.size()!=0){
            photos = new ArrayList<String>();
        }
        for(String url:urls){
            if(url!=null && !url.isEmpty() && !url.startsWith("http://")) {
                String photo = Constant.IMAGE_BASE + url;
                photos.add(photo);
            }
        }
        return photos;
    }

    @Override
    public Integer selectTimeLineCommentCount(byte commentType, Integer commentObjId, byte status) {
        return timeLineMapper.selectTimeLineCommentCount(commentType,commentObjId,status);
    }

    @Override
    public TimeLineVO readTimeLine(Integer timelineId) {
        return timeLineMapper.readTimeLine(timelineId);
    }

    @Override
    public List<User> selectLikeUsers(GodViewLikeVO likeVO) {
        return timeLineMapper.selectLikeUsers(likeVO);
    }

    @Override
    public List<Comment> selectComments(Byte type,Byte status,Integer timelineId, Integer commentNum) {
        return timeLineMapper.selectComments(type,status,timelineId,commentNum);
    }

    @Override
    public Integer selectById(Integer timelineId) {
        return timeLineMapper.selectById(timelineId);
    }
}
