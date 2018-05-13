package com.wishes.yearOld.model;

import java.util.Date;

public class Video {
    private Integer id;

    private String title;

    private String type;

    private String size;

    private String picUrl;

    private String fileUrl;

    private String playUrl;

    private String playPageUrl;

    private Integer playNum;

    private Integer duration;

    private String tag;

    private Date createTime;

    private Date updateTime;

    private Byte status;

    private String content;

    public Video(Integer id, String title, String type, String size, String picUrl, String fileUrl, String playUrl, String playPageUrl, Integer playNum, Integer duration, String tag, Date createTime, Date updateTime, Byte status, String content) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.size = size;
        this.picUrl = picUrl;
        this.fileUrl = fileUrl;
        this.playUrl = playUrl;
        this.playPageUrl = playPageUrl;
        this.playNum = playNum;
        this.duration = duration;
        this.tag = tag;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
        this.content = content;
    }

    public Video() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl == null ? null : playUrl.trim();
    }

    public String getPlayPageUrl() {
        return playPageUrl;
    }

    public void setPlayPageUrl(String playPageUrl) {
        this.playPageUrl = playPageUrl == null ? null : playPageUrl.trim();
    }

    public Integer getPlayNum() {
        return playNum;
    }

    public void setPlayNum(Integer playNum) {
        this.playNum = playNum;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}