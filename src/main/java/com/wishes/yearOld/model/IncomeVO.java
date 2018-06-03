package com.wishes.yearOld.model;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午7:57
 * To change this template use File | Settings | File Templates.
 */
public class IncomeVO {
    private Integer userid;

    private Integer type;

    private BigDecimal income;

    //getter and setter

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
