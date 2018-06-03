package com.wishes.yearOld.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * tgod_sales_record
 */
public class SalesRecord {
    /**
     * column : id
     * 售卖序号
     */
    private Integer id;

    /**
     * column : productId
     * 相关作品
     */
    private Integer productid;

    /**
     * column : salesTime
     * 售卖时间
     */
    private Date salestime;

    /**
     * column : buyerID
     * 购买者ID
     */
    private Integer buyerid;

    /**
     * column : salesType
     * 售卖类型：0 套图 | 1 头10 张 |  2  红包照片 | 3 头十张升级到套图
     */
    private Byte salestype;

    /**
     * column : price
     * 价格
     */
    private BigDecimal price;

    /**
     * column : photographerProfitRate
     * 写真师分润比
     */
    private BigDecimal photographerprofitrate;

    /**
     * column : modelID
     * 相关模特
     */
    private Integer modelid;

    /**
     * column : photoGrapherID
     * 相关写真师
     */
    private Integer photographerid;

    /**
     * column : currencyType
     * 货币类型：0 人民币 1 青豆
     */
    private Byte currencytype;

    /**
     * column : paytype
     * 0：支付宝  1 微信 
     */
    private Byte paytype;

    /**
     * 订单ID
     */
    private Integer orderId;

    public SalesRecord(Integer id, Integer productid, Date salestime, Integer buyerid, Byte salestype, BigDecimal price, BigDecimal photographerprofitrate, Integer modelid, Integer photographerid, Byte currencytype, Byte paytype, Integer orderId) {
        this.id = id;
        this.productid = productid;
        this.salestime = salestime;
        this.buyerid = buyerid;
        this.salestype = salestype;
        this.price = price;
        this.photographerprofitrate = photographerprofitrate;
        this.modelid = modelid;
        this.photographerid = photographerid;
        this.currencytype = currencytype;
        this.paytype = paytype;
        this.orderId = orderId;
    }

    public SalesRecord() {
        super();
    }

    public SalesRecord(Order order) {
        this.orderId = order.getId();
        this.productid = order.getAlbumId();
        this.salestime = order.getFinishTime();
        this.buyerid = order.getBuyerId();
        this.salestype = order.getOrderType();
        this.price = order.getTotalAmount();
        this.modelid = order.getActivityModel();
        this.currencytype = 0;
        this.paytype = order.getTradeType();
    }

    public SalesRecord(Order order,PhotoAlbum photoAlbum) {
        this.orderId = order.getId();
        this.productid = order.getAlbumId();
        this.salestime = order.getFinishTime();
        this.buyerid = order.getBuyerId();
        this.salestype = order.getAlbumLevel();
        this.price = order.getTotalAmount();
        this.photographerprofitrate = photoAlbum.getPhotoGrapherProfitRate();
        this.modelid = photoAlbum.getModelId();
        this.photographerid = photoAlbum.getPhotoGrapherId();
        this.currencytype = 0;
        this.paytype = order.getTradeType();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public Date getSalestime() {
        return salestime;
    }

    public void setSalestime(Date salestime) {
        this.salestime = salestime;
    }

    public Integer getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(Integer buyerid) {
        this.buyerid = buyerid;
    }

    public Byte getSalestype() {
        return salestype;
    }

    public void setSalestype(Byte salestype) {
        this.salestype = salestype;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPhotographerprofitrate() {
        return photographerprofitrate;
    }

    public void setPhotographerprofitrate(BigDecimal photographerprofitrate) {
        this.photographerprofitrate = photographerprofitrate;
    }

    public Integer getModelid() {
        return modelid;
    }

    public void setModelid(Integer modelid) {
        this.modelid = modelid;
    }

    public Integer getPhotographerid() {
        return photographerid;
    }

    public void setPhotographerid(Integer photographerid) {
        this.photographerid = photographerid;
    }

    public Byte getCurrencytype() {
        return currencytype;
    }

    public void setCurrencytype(Byte currencytype) {
        this.currencytype = currencytype;
    }

    public Byte getPaytype() {
        return paytype;
    }

    public void setPaytype(Byte paytype) {
        this.paytype = paytype;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}