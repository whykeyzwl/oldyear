package com.wishes.yearOld.model;

import java.util.Date;

public class SysMessage {
    private Integer id;

    private Byte msgType;

    private Byte status;

    private Date updatedTime;

    private Date publishTime;

    private String content;

    private Integer publishType;

    private String publishObj;

    private String title;

    public SysMessage(Integer id, Byte msgType, Byte status, Date updatedTime, Date publishTime, String content,Integer publishType,String publishObj,String title) {
        this.id = id;
        this.msgType = msgType;
        this.status = status;
        this.updatedTime = updatedTime;
        this.publishTime = publishTime;
        this.content = content;
        this.publishObj = publishObj;
        this.publishType = publishType;
        this.title = title;
    }

    public SysMessage() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getMsgType() {
        return msgType;
    }

    public void setMsgType(Byte msgType) {
        this.msgType = msgType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getPublishType() {
        return publishType;
    }

    public void setPublishType(Integer publishType) {
        this.publishType = publishType;
    }

    public String getPublishObj() {
        return publishObj;
    }

    public void setPublishObj(String publishObj) {
        this.publishObj = publishObj;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}