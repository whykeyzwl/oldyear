package com.wishes.yearOld.model;

import java.util.Date;

/**
 * tgod_timeline_photo
 */
public class TimeLinePhoto {
    /**
     * column : id
     * 
     */
    private Integer id;

    /**
     * column : timelineId
     * 
     */
    private Integer timelineid;

    /**
     * column : imageUrl
     * 
     */
    private String imageurl;

    /**
     * column : imageNo
     * 九宫格的排序 1-9
     */
    private Integer imageno;

    /**
     * column : createTime
     * 
     */
    private Date createtime;

    /**
     * column : status
     * 和个人动态的status字段同步
     */
    private Byte status;

    public TimeLinePhoto(Integer id, Integer timelineid, String imageurl, Integer imageno, Date createtime, Byte status) {
        this.id = id;
        this.timelineid = timelineid;
        this.imageurl = imageurl;
        this.imageno = imageno;
        this.createtime = createtime;
        this.status = status;
    }

    public TimeLinePhoto() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimelineid() {
        return timelineid;
    }

    public void setTimelineid(Integer timelineid) {
        this.timelineid = timelineid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl == null ? null : imageurl.trim();
    }

    public Integer getImageno() {
        return imageno;
    }

    public void setImageno(Integer imageno) {
        this.imageno = imageno;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}