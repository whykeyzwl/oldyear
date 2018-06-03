package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

public class Banner {
    private Integer id;

    private String imageUrl;

    private Byte bannerType;

    private Integer albumId;

    private Integer activityId;

    private Integer modelId;

    private Integer order;

    private String status;

    public Banner(Integer id, String imageUrl, Byte bannerType, Integer albumId, Integer activityId, Integer modelId,Integer order, String status) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.bannerType = bannerType;
        this.albumId = albumId;
        this.activityId = activityId;
        this.modelId = modelId;
        this.order = order;
        this.status = status;
    }

    public Banner() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageFullUrl() {
        if(imageUrl!=null && !imageUrl.isEmpty() && !imageUrl.startsWith("http://"))
            return Constant.IMAGE_BASE+imageUrl;
        return imageUrl;
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public Byte getBannerType() {
        return bannerType;
    }

    public void setBannerType(Byte bannerType) {
        this.bannerType = bannerType;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
}