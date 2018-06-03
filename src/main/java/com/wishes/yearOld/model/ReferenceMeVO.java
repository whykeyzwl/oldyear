package com.wishes.yearOld.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/11/9
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 * at我的消息
 */
public class ReferenceMeVO {

    private Reference reference;//@基本信息

    private Comment comment;//@是评论

    private PhotoAlbum photoAlbum;//图集

    private TimeLineVO com_timeLineVO;//评论是个人动态
    private TimeLineVO ref_timeLineVO;//@是个人动态

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

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

    public TimeLineVO getCom_timeLineVO() {
        return com_timeLineVO;
    }

    public void setCom_timeLineVO(TimeLineVO com_timeLineVO) {
        this.com_timeLineVO = com_timeLineVO;
    }

    public TimeLineVO getRef_timeLineVO() {
        return ref_timeLineVO;
    }

    public void setRef_timeLineVO(TimeLineVO ref_timeLineVO) {
        this.ref_timeLineVO = ref_timeLineVO;
    }
}
