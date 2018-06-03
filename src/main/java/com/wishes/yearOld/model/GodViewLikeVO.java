package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

import java.util.Date;

/**
 * Created by zouyu on 2016/10/14.
 */
public class GodViewLikeVO {

    /**
     * id 主键
     */
    private Integer id;

    /**
     * userId 点赞用户id
     */
    private Integer userId;

    /**
     * likeId 被赞图集id
     */
    private Integer likeId;

    /**
     * likeType 女神:1 图集:2 动态:3
     */
    private Integer likeType;

    /**
     * createTime 创建时间
     */
    private Date createTime;

    /**
     * likeNum 查询数量
     */
    private Integer likeNum;

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

    public Integer getLikeId() {
        return likeId;
    }

    public void setLikeId(Integer likeId) {
        this.likeId = likeId;
    }

    public Integer getLikeType() {
        return likeType;
    }

    public void setLikeType(Integer likeType) {
        this.likeType = likeType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }
}
