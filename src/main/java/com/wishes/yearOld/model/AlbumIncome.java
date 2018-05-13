package com.wishes.yearOld.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/11/23
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */
public class AlbumIncome {

    //今日收入
    private BigDecimal todayIncome;
    //昨日收入
    private BigDecimal yesterdayIncome;
    //总收入
    private BigDecimal amountIncome;

    //购买此图集的用户
    private List<Integer> list;

    //购买此图集用户数量
    private Integer buynum;

    public BigDecimal getTodayIncome() {
        if(todayIncome == null){
            todayIncome = new BigDecimal("0.00");
        }
        return todayIncome;
    }

    public void setTodayIncome(BigDecimal todayIncome) {
        this.todayIncome = todayIncome;
    }

    public BigDecimal getYesterdayIncome() {
        if(yesterdayIncome == null){
            yesterdayIncome = new BigDecimal("0.00");
        }
        return yesterdayIncome;
    }

    public void setYesterdayIncome(BigDecimal yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public BigDecimal getAmountIncome() {
        if(amountIncome == null){
            amountIncome = new BigDecimal("0.00");
        }
        return amountIncome;
    }

    public void setAmountIncome(BigDecimal amountIncome) {
        this.amountIncome = amountIncome;
    }

    public Integer getBuynum() {
        return buynum;
    }

    public void setBuynum(Integer buynum) {
        this.buynum = buynum;
    }
}
