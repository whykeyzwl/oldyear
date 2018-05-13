package com.wishes.yearOld.model;

import java.util.Date;

/**
 * tgod_timeline
 */
public class TimeLine {
    /**
     * column : id
     * 序号
     */
    private Integer id;

    /**
     * column : userId
     * 用户ID
     */
    private Integer userid;

    /**
     * column : content_type
     * 0 图文 1 红包图片 2 作品动态
     */
    private Byte contentType;

    /**
     * column : albumId
     * 类型为图集时有值，否则为空
     */
    private Integer albumid;

    /**
     * column : created_time
     * 创建时间
     */
    private Date createdTime;

    /**
     * column : status
     * 状态 预留字段给删除、屏蔽等多个状态，默认0为正常状态
     */
    private Byte status;

    /**
     * column : content
     * 内容： 图文类型对应发表文字，图集对应图集标题
     */
    private String content;

    public TimeLine(Integer id, Integer userid, Byte contentType, Integer albumid, Date createdTime, Byte status, String content) {
        this.id = id;
        this.userid = userid;
        this.contentType = contentType;
        this.albumid = albumid;
        this.createdTime = createdTime;
        this.status = status;
        this.content = content;
    }

    private String picUrl1;

    private String picUrl2;

    private String picUrl3;

    private String picUrl4;

    private String picUrl5;

    private String picUrl6;

    private String picUrl7;

    private String picUrl8;

    private String picUrl9;

    public TimeLine() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Byte getContentType() {
        return contentType;
    }

    public void setContentType(Byte contentType) {
        this.contentType = contentType;
    }

    public Integer getAlbumid() {
        return albumid;
    }

    public void setAlbumid(Integer albumid) {
        this.albumid = albumid;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPicUrl1() {
        return picUrl1;
    }

    public void setPicUrl1(String picUrl1) {
        this.picUrl1 = picUrl1;
    }

    public String getPicUrl2() {
        return picUrl2;
    }

    public void setPicUrl2(String picUrl2) {
        this.picUrl2 = picUrl2;
    }

    public String getPicUrl3() {
        return picUrl3;
    }

    public void setPicUrl3(String picUrl3) {
        this.picUrl3 = picUrl3;
    }

    public String getPicUrl4() {
        return picUrl4;
    }

    public void setPicUrl4(String picUrl4) {
        this.picUrl4 = picUrl4;
    }

    public String getPicUrl5() {
        return picUrl5;
    }

    public void setPicUrl5(String picUrl5) {
        this.picUrl5 = picUrl5;
    }

    public String getPicUrl6() {
        return picUrl6;
    }

    public void setPicUrl6(String picUrl6) {
        this.picUrl6 = picUrl6;
    }

    public String getPicUrl7() {
        return picUrl7;
    }

    public void setPicUrl7(String picUrl7) {
        this.picUrl7 = picUrl7;
    }

    public String getPicUrl8() {
        return picUrl8;
    }

    public void setPicUrl8(String picUrl8) {
        this.picUrl8 = picUrl8;
    }

    public String getPicUrl9() {
        return picUrl9;
    }

    public void setPicUrl9(String picUrl9) {
        this.picUrl9 = picUrl9;
    }
}