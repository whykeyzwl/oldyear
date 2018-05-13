package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

import java.math.BigDecimal;
import java.util.List;

/**
 * 活动模特
 * Created by tmg-yesky on 2016/9/20.
 */
public class ActivityModel extends Resource{

    /**
     * ID
     */
    private Integer id;

    /**
     * 活动ID
     */
    private Integer activityId;

    /**
     * 模特ID
     */
    private Integer modelId;

    /**
     * 模特昵称
     */
    private String modelNickName;

    /**
     * 模特封面
     */
    private String covor;

    /**
     * 妹子介绍
     */
    private String modelIntro;

    /**
     * 已筹集金额
     */
    private BigDecimal raised;

    /**
     * 是否成功
     */
    private Integer successed;

    /**
     * 支持人数
     */
    private Integer supporters;

    /**
     * 筹集进度
     */
    private BigDecimal progress;

    /**
     * 活动相关信息
     */
    private Activity activity;

    /**
     * 是否被当前用户关注
     */
    private boolean isFocusByCurUser;

    private Integer ruleCount,albumCount,supportTimes,supporterCount;

    private List<ActivityRule> rules;

    private List<PhotoAlbum> albums;

    private List<ActivitySupporter> supportTimeList; //支持行为

    private List<ActivitySupporter> supporterList; //支持者排行

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

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelNickName() {
        return modelNickName;
    }

    public void setModelNickName(String modelNickName) {
        this.modelNickName = modelNickName;
    }

    public String getCoverUrl(){
        if(covor!=null && !covor.isEmpty() && !covor.startsWith("http://"))
            return Constant.IMAGE_BASE+covor;
        return covor;
    }

//    public String getCovor() {
//        return covor;
//    }

    public void setCovor(String covor) {
        this.covor = covor;
    }

    public String getModelIntro() {
        return modelIntro;
    }

    public void setModelIntro(String modelIntro) {
        this.modelIntro = modelIntro;
    }

    public BigDecimal getRaised() {
        return raised;
    }

    public void setRaised(BigDecimal raised) {
        this.raised = raised;
    }

    public Integer getSuccessed() { return successed; }

    public void setSuccessed(Integer successed) {
        this.successed = successed;
    }

    public Integer getSupporters() {
        return supporters;
    }

    public void setSupporters(Integer supporters) {
        this.supporters = supporters;
    }

    public String getProgress() {
        return String.format("%.0f%%",progress);
    }

    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

    public boolean isRaisedSuccess(){
        return progress.compareTo(new BigDecimal(100))>=0;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<ActivityRule> getRules() {
        return rules;
    }

    public void setRules(List<ActivityRule> rules) {
        this.rules = rules;
    }

    public Integer getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(Integer albumCount) {
        this.albumCount = albumCount;
    }

    public List<PhotoAlbum> getAlbums() {
        return albums;
    }

    public void setAlbums(List<PhotoAlbum> albums) {
        this.albums = albums;
    }

    public Integer getRuleCount() {
        return ruleCount;
    }

    public void setRuleCount(Integer ruleCount) {
        this.ruleCount = ruleCount;
    }


    public Integer getSupporterCount() {
        return supporterCount;
    }

    public void setSupporterCount(Integer supporterCount) {
        this.supporterCount = supporterCount;
    }

    public List<ActivitySupporter> getSupporterList() {
        return supporterList;
    }

    public void setSupporterList(List<ActivitySupporter> supporterList) {
        this.supporterList = supporterList;
    }

    public Integer getSupportTimes() {
        return supportTimes;
    }

    public void setSupportTimes(Integer supportTimes) {
        this.supportTimes = supportTimes;
    }

    public List<ActivitySupporter> getSupportTimeList() {
        return supportTimeList;
    }

    public void setSupportTimeList(List<ActivitySupporter> supportTimeList) {
        this.supportTimeList = supportTimeList;
    }

    public boolean isFocusByCurUser() {
        return isFocusByCurUser;
    }

    public void setFocusByCurUser(boolean focusByCurUser) {
        isFocusByCurUser = focusByCurUser;
    }
}
