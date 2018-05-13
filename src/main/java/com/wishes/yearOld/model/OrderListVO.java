package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-11-8
 * Time: 下午3:47
 * To change this template use File | Settings | File Templates.
 */
public class OrderListVO {

    /**
     * 订单ID
     */
    private Integer orderId;

    private Integer buyerId;
    /**
     * 下单时间
     */
    private Date orderDate;
    /**
     * 购买类型（1：购买图集 2：购买活动）
     */
    private Integer orderType;

    /**
     * 购买价钱
     */
    private BigDecimal totalMount;
    /**
     *
     * 购买图集等级（1：一级解锁 2：二级解锁，3：套图）
     */
    private Byte albumLevel;

    private Integer photoAlbumID;

    private String photoAlbumTitle;

    private String photoAlbumDescription;

    private Integer modelId;

    private String modelName;

    private String photoAlbumModelCover;

    private Integer activityId;

    private BigDecimal activityTarget;

    private BigDecimal activityModelRaised;

    private Integer activityModelSupporters;

    private Date activityEndTime;

    private String activityModelCover;

    private String activityModelIntro;

    private String activityTitle;

    private String activityRemainingTime;

    private Byte activitySuccess;

    public Byte getActivitySuccess() {
        return activitySuccess;
    }

    public void setActivitySuccess(Byte activitySuccess) {
        this.activitySuccess = activitySuccess;
    }

    public String getActivityRemainingTime() {
        if (activityEndTime == null) {
            return null;
        }
        Date nowTime = new Date();
        if (activityEndTime.getTime() <= nowTime.getTime()) {
            return "活动已结束";
        } else {
            return DateUtil.getTimeDifference(nowTime, activityEndTime);
        }
    }

    public void setActivityRemainingTime(String activityRemainingTime) {
        this.activityRemainingTime = activityRemainingTime;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getTotalMount() {
        return totalMount;
    }

    public void setTotalMount(BigDecimal totalMount) {
        this.totalMount = totalMount;
    }

    public Byte getAlbumLevel() {
        return albumLevel;
    }

    public void setAlbumLevel(Byte albumLevel) {
        this.albumLevel = albumLevel;
    }

    public Integer getPhotoAlbumID() {
        return photoAlbumID;
    }

    public void setPhotoAlbumID(Integer photoAlbumID) {
        this.photoAlbumID = photoAlbumID;
    }

    public String getPhotoAlbumTitle() {
        return photoAlbumTitle;
    }

    public void setPhotoAlbumTitle(String photoAlbumTitle) {
        this.photoAlbumTitle = photoAlbumTitle;
    }

    public String getPhotoAlbumDescription() {
        return photoAlbumDescription;
    }

    public void setPhotoAlbumDescription(String photoAlbumDescription) {
        this.photoAlbumDescription = photoAlbumDescription;
    }

    public String getPhotoAlbumModelCover() {
        if(photoAlbumModelCover!=null && !photoAlbumModelCover.isEmpty() && !photoAlbumModelCover.startsWith("http://"))
            return Constant.IMAGE_BASE+photoAlbumModelCover;
        return photoAlbumModelCover;
    }

    public void setPhotoAlbumModelCover(String photoAlbumModelCover) {
        this.photoAlbumModelCover = photoAlbumModelCover;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityModelCover() {
        if(activityModelCover!=null && !activityModelCover.isEmpty() && !activityModelCover.startsWith("http://"))
            return Constant.IMAGE_BASE+activityModelCover;
        return activityModelCover;
    }

    public void setActivityModelCover(String activityModelCover) {
        this.activityModelCover = activityModelCover;
    }

    public String getActivityModelIntro() {
        return activityModelIntro;
    }

    public void setActivityModelIntro(String activityModelIntro) {
        this.activityModelIntro = activityModelIntro;
    }

    public BigDecimal getActivityTarget() {
        return activityTarget;
    }

    public void setActivityTarget(BigDecimal activityTarget) {
        this.activityTarget = activityTarget;
    }

    public BigDecimal getActivityModelRaised() {
        return activityModelRaised;
    }

    public void setActivityModelRaised(BigDecimal activityModelRaised) {
        this.activityModelRaised = activityModelRaised;
    }

    public Integer getActivityModelSupporters() {
        return activityModelSupporters;
    }

    public void setActivityModelSupporters(Integer activityModelSupporters) {
        this.activityModelSupporters = activityModelSupporters;
    }

    public Date getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Date activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
