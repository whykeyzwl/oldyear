package com.wishes.yearOld.model;

import java.util.Date;

public class UserAlbumLog {
    private Integer id;

    private Integer userid;

    private Integer albumid;

    private Integer star;

    private String tags;

    private String comment;

    private Date createTime;

    public UserAlbumLog(Integer id, Integer userid, Integer albumid, Integer star, String tags, String comment, Date createTime) {
        this.id = id;
        this.userid = userid;
        this.albumid = albumid;
        this.star = star;
        this.tags = tags;
        this.comment = comment;
        this.createTime = createTime;
    }

    public UserAlbumLog() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getAlbumid() {
        return albumid;
    }

    public void setAlbumid(Integer albumid) {
        this.albumid = albumid;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}