package com.wishes.yearOld.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
public class UserTradeRecord {

    /**
     * 主键id
     */
    private Integer id;

    /**
     *用户id
     */
    private Integer userid;

    /**
     * 本次交易的流水号
     */
    private Integer salesRecordId;

    /**
     * 交易时间
     */

    private Date recordTime;

    /**
     * 金额。正值为收入，负值为支出
     */
    private BigDecimal amount;

    /**
     * 交易说明
     */
    private String description;

    /**
     * 交易作品ID
     */
    private Integer photoalbumID;

    /**
     * 作品封面
     */
    private String photoalbumCover;

    /**
     * 相关的销售记录
     */
    private SalesRecord salesRecord;

    /**
     * 作品title
     */
    private String photoalbumTitle;


    //getter and setter
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

    public Integer getSalesRecordId() {
        return salesRecordId;
    }

    public void setSalesRecordId(Integer salesRecordId) {
        this.salesRecordId = salesRecordId;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPhotoalbumID() {
        return photoalbumID;
    }

    public void setPhotoalbumID(Integer photoalbumID) {
        this.photoalbumID = photoalbumID;
    }

    public String getPhotoalbumCover() {
        return photoalbumCover;
    }

    public void setPhotoalbumCover(String photoalbumCover) {
        this.photoalbumCover = photoalbumCover;
    }

    public SalesRecord getSalesRecord() {
        return salesRecord;
    }

    public void setSalesRecord(SalesRecord salesRecord) {
        this.salesRecord = salesRecord;
    }

    public String getPhotoalbumTitle() {
        return photoalbumTitle;
    }

    public void setPhotoalbumTitle(String photoalbumTitle) {
        this.photoalbumTitle = photoalbumTitle;
    }
}
