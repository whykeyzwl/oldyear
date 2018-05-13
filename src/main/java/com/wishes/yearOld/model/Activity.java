package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.DateUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 旅拍
 * Created by tmg-yesky on 2016/9/20.
 */
public class Activity extends Resource{

    /**
     * ID
     */
    private Integer id;

    /**
     * 活动名称
     */
    private String title;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 封面图(竖图)
     */
    private String coverProcess;

    /**
     * 封面图(横图)
     */
    private String coverFinished;

    /**
     * 活动介绍
     */
    private String introduce;

    /**
     * 参与写真师
     */
    private String photoGrathers;

    /**
     * 众筹目标金额
     */
    private BigDecimal raiseTarget;

    /**
     * 已众筹金额
     */
    private BigDecimal raised;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 是否结束(false:未结束 true:结束)
     */
    private Boolean finished;

    private List<ActivityModel> models;

    private List<PhotoAlbum> albums;

    private List<User> photographer;

    private List<ActivitySupporter> supporters;

    private Integer modelCount, albumCount, supportCount;

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
        this.title = title;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPhotoGrathers() {
        return photoGrathers;
    }

    public void setPhotoGrathers(String photoGrathers) {
        this.photoGrathers = photoGrathers;
    }

    public BigDecimal getRaiseTarget() {
        return raiseTarget;
    }

    public void setRaiseTarget(BigDecimal raiseTarget) {
        this.raiseTarget = raiseTarget;
    }

    public BigDecimal getRaised() {
        return raised;
    }

    public void setRaised(BigDecimal raised) {
        this.raised = raised;
    }

    public List<ActivityModel> getModels() {
        return models;
    }

    public void setModels(List<ActivityModel> models) {
        this.models = models;
    }

    public String getCoverProcessUrl() {
        if(coverProcess!=null && !coverProcess.isEmpty() && !coverProcess.startsWith("http://"))
            return Constant.IMAGE_BASE+coverProcess;
        return coverProcess;
    }

    public void setCoverProcess(String coverProcess) {
        this.coverProcess = coverProcess;
    }

    public String getCoverFinishedUrl() {
        if(coverFinished!=null && !coverFinished.isEmpty() && !coverFinished.startsWith("http://"))
            return Constant.IMAGE_BASE+coverFinished;
        return coverFinished;
    }

    public void setCoverFinished(String coverFinished) {
        this.coverFinished = coverFinished;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getFinished() {
        if(this.endTime != null){
            if(new Date().before(endTime)){
                return false;
            }else{
                return true;
            }
        }
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public List<PhotoAlbum> getAlbums() {
        return albums;
    }

    public void setAlbums(List<PhotoAlbum> albums) {
        this.albums = albums;
    }

    public List<User> getPhotographer() {
        return photographer;
    }

    public void setPhotographer(List<User> photographer) {
        this.photographer = photographer;
    }

    public String getRemindTime(){
        Date now = new Date();
        if(endTime.compareTo(now)<=0)
            return "已结束";

        return DateUtil.getTimeDifference(now,endTime);
    }

    public Integer getModelCount() {
        return modelCount;
    }

    public void setModelCount(Integer modelCount) {
        this.modelCount = modelCount;
    }

    public Integer getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(Integer albumCount) {
        this.albumCount = albumCount;
    }

    public Integer getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(Integer supportCount) {
        this.supportCount = supportCount;
    }

    public List<ActivitySupporter> getSupporters() {
        return supporters;
    }

    public void setSupporters(List<ActivitySupporter> supporters) {
        this.supporters = supporters;
    }
}
