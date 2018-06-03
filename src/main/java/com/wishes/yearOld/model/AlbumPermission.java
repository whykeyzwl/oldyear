package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2016/11/9
 * Description:
 */
public class AlbumPermission {
    private Integer userId;
    private Integer albumId;
    private Integer permitType;
    private String permitDesp;
    private Integer userLevel;
    private Integer userDownlimit;
    private Integer userTotalDownlimit;
    private BigDecimal buyPrice1;
    private BigDecimal buyPrice2;
    private Integer qingdouPrice;

    public boolean isOperEnabled() {
        return userLevel==null || userLevel<2;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getPermitType() {
        return permitType;
    }

    public void setPermitType(Integer permitType) {
        this.permitType = permitType;
    }

    public String getPermitDesp() {
        return permitDesp;
    }

    public void setPermitDesp(String permitDesp) {
        this.permitDesp = permitDesp;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getVisiblePages() {
        if(userLevel==null || userLevel<1)
            return Constant.PHOTO_PAGE_FREE;

        if(userLevel == 1)
            return Constant.PHOTO_PAGE_1;

        return Constant.PHOTO_PAGE_ALL;
    }

    public Integer getUserDownlimit() {
        return userDownlimit;
    }

    public void setUserDownlimit(Integer userDownlimit) {
        this.userDownlimit = userDownlimit;
    }

    public BigDecimal getBuyPrice1() {
        return buyPrice1;
    }

    public void setBuyPrice1(BigDecimal buyPrice1) {
        this.buyPrice1 = buyPrice1;
    }

    public BigDecimal getBuyPrice2() {
        return buyPrice2;
    }

    public void setBuyPrice2(BigDecimal buyPrice2) {
        this.buyPrice2 = buyPrice2;
    }

    public Integer getQingdouPrice() {
        return qingdouPrice;
    }

    public void setQingdouPrice(Integer qingdouPrice) {
        this.qingdouPrice = qingdouPrice;
    }

    public Integer getUserTotalDownlimit() {
        return userTotalDownlimit;
    }

    public void setUserTotalDownlimit(Integer userTotalDownlimit) {
        this.userTotalDownlimit = userTotalDownlimit;
    }
}
