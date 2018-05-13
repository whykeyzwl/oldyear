package com.wishes.yearOld.model;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-10-21
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public class GoodsVO {
    /**
     * 所要购买的图集的id
     */
    private  Integer albumId;

    /**
     * 所要购买的图集的等级（0 套图 | 1 头10 张 |  2  红包照片 | 3 头十张升级到套图）
     */
    private Byte albumLevel;

    /**
     * 所要购买活动的id
     */
    private Integer activityId;

    /**
     * 所要购买活动的规则id
     */
    private Integer activityRule;

    /**
     * 所要购买活动中的具体模特id
     */
    private Integer activityModel;

    /**
     * 购买类型（1：图集 2：活动）
     */
    private Byte goodsType;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 商品数量
     */
    private Integer quantity = 1;
    
    /**
     * 页面类型区分
     */
    private Integer pageType;
    
    /**
     * 活动无偿支持价格
     */
    private BigDecimal activityFreeSupportMoney;

    public BigDecimal getActivityFreeSupportMoney() {
        return activityFreeSupportMoney;
    }

    public void setActivityFreeSupportMoney(BigDecimal activityFreeSupportMoney) {
        this.activityFreeSupportMoney = activityFreeSupportMoney;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Byte getAlbumLevel() {
        return albumLevel;
    }

    public void setAlbumLevel(Byte albumLevel) {
        this.albumLevel = albumLevel;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(Integer activityRule) {
        this.activityRule = activityRule;
    }

    public Integer getActivityModel() {
        return activityModel;
    }

    public void setActivityModel(Integer activityModel) {
        this.activityModel = activityModel;
    }

    public Byte getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Byte goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

	public Integer getPageType() {
		return pageType;
	}

	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
    
}
