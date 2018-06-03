package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.StringUtils;

import java.util.Date;

/**
 * Created by tmg-yesky on 2016/9/20.
 */
public class AlbumViewLog {

    /**
     * ID
     */
    private Integer id;

    /**
     * 写真集ID
     */
    private Integer albumId;

    /**
     * 写真集封面
     */
    private String coverUrl;

    /**
     * 模特ID
     */
    private Integer modelId;

    /**
     * 模特名称
     */
    private String modelName;

    /**
     * 写真师ID
     */
    private Integer photoGrapherId;

    /**
     * 浏览用户ID
     */
    private Integer userId;

    /**
     * 浏览日期
     */
    private String viewDate;

    /**
     * 浏览时间
     */
    private Date viewTime;

    private String firstPhoto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getPhotoGrapherId() {
        return photoGrapherId;
    }

    public void setPhotoGrapherId(Integer photoGrapherId) {
        this.photoGrapherId = photoGrapherId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }

    public String getCoverUrl() {
        if(StringUtils.isEmpty(coverUrl))
            coverUrl = firstPhoto;
        if(StringUtils.isNotEmpty(coverUrl) && !coverUrl.startsWith("http://"))
            return Constant.IMAGE_BASE+coverUrl;
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getViewDate() {
        return viewDate;
    }

    public void setViewDate(String viewDate) {
        this.viewDate = viewDate;
    }

    public void setFirstPhoto(String firstPhoto) {
        this.firstPhoto = firstPhoto;
    }
}
