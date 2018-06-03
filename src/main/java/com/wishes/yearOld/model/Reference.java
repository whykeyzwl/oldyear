package com.wishes.yearOld.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/24
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class Reference {

    private Integer Id;

    private Integer referId;

    private Integer referType;

    private Integer referUserId;

    private String content;

    private Date commentTime;

    private Byte status;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getReferId() {
        return referId;
    }

    public void setReferId(Integer referId) {
        this.referId = referId;
    }

    public Integer getReferType() {
        return referType;
    }

    public void setReferType(Integer referType) {
        this.referType = referType;
    }

    public Integer getReferUserId() {
        return referUserId;
    }

    public void setReferUserId(Integer referUserId) {
        this.referUserId = referUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
