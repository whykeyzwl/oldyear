package com.wishes.yearOld.model;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/11/10
 * Time: 10:46
 * To change this template use File | Settings | File Templates.
 * 评论我的消息
 */
public class CommentMeVO {

    private Comment comment;

    private PhotoAlbum photoAlbum;

    private TimeLineVO timeLineVO;

    private User fromUser;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public PhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }

    public TimeLineVO getTimeLineVO() {
        return timeLineVO;
    }

    public void setTimeLineVO(TimeLineVO timeLineVO) {
        this.timeLineVO = timeLineVO;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }
}
