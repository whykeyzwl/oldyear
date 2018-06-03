package com.wishes.yearOld.model;

import java.util.Date;

public class RecordDownLimit {
    private Integer id;

    private Integer userId;

    private Integer activityId;

    private Integer modelId;

    private Integer ruleId;

    private Byte status;

    private Integer albumId;

    private Date createTime;

    private Date consumeTime;

    public RecordDownLimit(Integer id, Integer userId, Integer activityId, Integer modelId, Integer ruleId, Byte status, Integer albumId, Date createTime, Date consumeTime) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.modelId = modelId;
        this.ruleId = ruleId;
        this.status = status;
        this.albumId = albumId;
        this.createTime = createTime;
        this.consumeTime = consumeTime;
    }

    public RecordDownLimit() {
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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Date getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Date consumeTime) {
        this.consumeTime = consumeTime;
    }
}