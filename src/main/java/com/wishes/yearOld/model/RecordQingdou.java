package com.wishes.yearOld.model;

import java.util.Date;

public class RecordQingdou {
    private Integer id;

    private Integer userId;

    private Integer qingdouAmount;

    private Byte recType;

    private Integer albumId;

    private Date createTime;

    private String title;//图集title

    private Integer current;//是否今天

    public RecordQingdou(Integer id, Integer userId, Integer qingdouAmount, Byte recType,
                         Integer albumId, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.qingdouAmount = qingdouAmount;
        this.recType = recType;
        this.albumId = albumId;
        this.createTime = createTime;
    }

    public RecordQingdou() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQingdouAmount() {
        return qingdouAmount;
    }

    public void setQingdouAmount(Integer qingdouAmount) {
        this.qingdouAmount = qingdouAmount;
    }

    public Byte getRecType() {
        return recType;
    }

    public void setRecType(Byte recType) {
        this.recType = recType;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
}