package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.StringUtils;

import java.util.Date;

/**
 * Created by wanghh on 2017/3/20 0020.
 */
public class UsersCollection {

    private Integer pageNo;
    private Integer pageSize;
    private Integer start;
    private Integer userGroup;

    private Integer id;

    private Integer userId;

    private Integer collectionId;

    private Byte collectionType;

    private Date createTime;

    private Integer albumId;//图集ID

    private Integer pages;//图集图片张数

    private String icon;//当前用户头像

    private String name;//用户昵称

    private boolean collection;

    private String cover;//图集封面

    private Integer modelID;//模特ID

    private String modelName;//模特姓名

    private String cert;//认证方式

    private String title;//图集标题

    private String description;//图集详细介绍

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Integer userGroup) {
        this.userGroup = userGroup;
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

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    public Byte getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(Byte collectionType) {
        this.collectionType = collectionType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getIcon() {
        if (icon!=null && !icon.isEmpty() && icon.startsWith("/"))
            icon = icon.substring(1);
        if(icon!=null && !icon.isEmpty() && !icon.startsWith("http://")) {
            return Constant.IMAGE_BASE + icon;
        }
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public String getCover() {
        if (cover!=null && !cover.isEmpty() && cover.startsWith("/"))
            cover = cover.substring(1);
        if(cover!=null && !cover.isEmpty() && !cover.startsWith("http://")) {
            return Constant.IMAGE_BASE + cover;
        }
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCert() {
        if(StringUtils.isEmpty(cert)){
            cert = "";
        }
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getTitle() {
        if(StringUtils.isEmpty(title))
            title = "";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if(StringUtils.isEmpty(description))
            description = "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
