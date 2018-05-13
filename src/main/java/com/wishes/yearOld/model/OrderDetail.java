package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-11-10
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
public class OrderDetail {

    private Integer id;

    private String orderNumber;

    private String tradeNo;

    private Integer tradeType;

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

    private String outRefundNo;

    private Date refundTime;

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }
}
