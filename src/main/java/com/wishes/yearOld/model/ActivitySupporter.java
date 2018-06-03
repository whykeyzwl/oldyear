package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动支持者
 * Created by tmg-yesky on 2016/9/20.
 */
public class ActivitySupporter extends Resource{

    /**
     * ID
     */
    private Integer id;

    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     * 规则ID
     */
    private Integer ruleId;

    /**
     * 模特ID
     */
    private Integer modelId;

    /**
     * 支持金额
     */
    private BigDecimal money;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String face;

    /**
     * 支持时间
     */
    private Date suportTime;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 支持总金额
     */
    private BigDecimal totalMoney;

    /**
     * 是否被当前用户关注
     */
    private boolean focusByCurUser;

    private Integer userLevel;

    public ActivitySupporter() {
    }

    public ActivitySupporter(Order order,User user) {
        this.orderId = order.getId();
        this.activityId = order.getActivityId();
        this.ruleId = order.getActivityRule();
        this.modelId = order.getActivityModel();
        this.money = order.getTotalAmount();
        this.userId = order.getBuyerId();
        this.nickName = user.getNickName();
        this.face = user.getFaceUrl();
        this.suportTime = order.getCreateTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

//    public String getFace() {
//        return face;
//    }

    public String getFaceUrl() {
        if(face!=null && !face.isEmpty() && !face.startsWith("http://"))
            return Constant.IMAGE_BASE+face;
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Date getSuportTime() {
        return suportTime;
    }

    public void setSuportTime(Date suportTime) {
        this.suportTime = suportTime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public boolean isFocusByCurUser() {
        return focusByCurUser;
    }

    public void setFocusByCurUser(boolean focusByCurUser) {
        this.focusByCurUser = focusByCurUser;
    }

    public Integer getUserLevel() {
        return userLevel==null?1:userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }
}
