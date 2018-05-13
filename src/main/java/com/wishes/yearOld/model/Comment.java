package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/24
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class Comment {

    private Integer pageNo;
    private Integer pageSize;
    private Integer desc;
    private Integer start;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 评论用户
     */
    private Integer fromUserId;

    /**
     * 评论用户名
     */
    private String fromUserName;

    /**
     * 评论用户头像
     */
    private String fromFaceUrl;

    /**
     * 评论目标用户
     */
    private Integer toUserId;

    /**
     * 评论目标用户名
     */
    private String toUserName;

    /**
     * 评论类型 图集:1 个人动态:2
     */
    private Byte type;

    /**
     * 评论对象Id
     */
    private Integer objId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 是否有"@"对象
     */
    private Byte hasReferUser;

    /**
     * 评论中"@"的对象id,用","分隔
     */
    private String referUsers;

    /**
     * 评论中"@"的对象id
     */
    private Integer[] referUserIds;

    /**
     * 预留字段 默认为零
     */
    private Byte status;

    /**
     * 评论时间
     */
    private Date commentTime;

    private Integer consumerType;

    private Integer userLevel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getObjId() {
        return objId;
    }

    public void setObjId(Integer objId) {
        this.objId = objId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getHasReferUser() {
        return (referUsers != null && !"".equals(referUsers))? Constant.REFER_USER_YES:Constant.REFER_USER_NO;
    }

    public void setHasReferUser(Byte hasReferUser) {
        this.hasReferUser = hasReferUser;
    }

    public String getReferUsers() {
        return referUsers;
    }

    public void setReferUsers(String referUsers) {
        this.referUsers = referUsers;
    }

    public Integer[] getReferUserIds() {
        if(referUsers != null && !"".equals(referUsers)){
            String[] referUserIdStr = referUsers.split(",");
            referUserIds = new Integer[referUserIdStr.length];
            for(int i=0;i<referUserIdStr.length;i++){
                referUserIds[i] = Integer.valueOf(referUserIdStr[i]);
            }
        }
        return referUserIds;
    }

    public void setReferUserIds(Integer[] referUserIds) {
        this.referUserIds = referUserIds;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromFaceUrl() {
        if(fromFaceUrl!=null && !fromFaceUrl.isEmpty() && !fromFaceUrl.startsWith("http://")){
            fromFaceUrl= Constant.IMAGE_BASE+fromFaceUrl;
        }
        return fromFaceUrl;
    }

    public void setFromFaceUrl(String fromFaceUrl) {
        this.fromFaceUrl = fromFaceUrl;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public Integer getPageNo() {
        if(pageNo!=null && pageNo>0)
            return pageNo;
        else
            return  Constant.DEF_PAGE_NUM;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        if( pageSize!=null &&  pageSize>0)
            return  pageSize;
        else
            return  Constant.DEF_PAGE_SIZE;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getDesc() {
        return desc;
    }

    public void setDesc(Integer desc) {
        this.desc = desc;
    }

    public Integer getStart() {
        return (getPageNo()-1)*getPageSize();
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getUserLevel() {
        return userLevel==null?1:userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(Integer consumerType) {
        this.consumerType = consumerType;
    }
}
