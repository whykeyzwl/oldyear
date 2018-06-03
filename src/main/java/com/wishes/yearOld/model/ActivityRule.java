package com.wishes.yearOld.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 旅拍规则
 * Created by tmg-yesky on 2016/9/20.
 */
public class ActivityRule extends Resource{

    /**
     * ID
     */
    private Integer id;

    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     * 支持金额
     */
    private BigDecimal money;

    /**
     * 免费下载套图数
     */
    private Integer downloadLimit;

    /**
     * 限制人数
     */
    private Integer suporterLimit;

    /**
     * 规则介绍
     */
    private String introduce;

    /**
     * 规则排序
     */
    private Integer order;

    private Integer supportTimes;

    private boolean hasSupported;

    private boolean isFinish;

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(Integer downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public Integer getSuporterLimit() {
        return suporterLimit;
    }

    public void setSuporterLimit(Integer suporterLimit) {
        this.suporterLimit = suporterLimit;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean isRandomFeeSupport() {
        return money.doubleValue()==0 && downloadLimit==0 && suporterLimit==-1;
    }

    public Integer getSupportTimes() {
        return supportTimes;
    }

    public void setSupportTimes(Integer supportTimes) {
        this.supportTimes = supportTimes;
    }

    public boolean isHasSupported() {
        return hasSupported;
    }

    public void setHasSupported(boolean hasSupported) {
        this.hasSupported = hasSupported;
    }
}
