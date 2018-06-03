package com.wishes.yearOld.model;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class UserAccount {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户组：0:普通用户 2:模特 3: 摄影师
     */
    private Integer user_group;

    /**
     *总收入
     */
    private BigDecimal amount;

    /**
     * 已提取现金
     */
    private BigDecimal cashout;

    /**
     * 余额
     */
    private BigDecimal balance;


    // getter and setter

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

    public Integer getUser_group() {
        return user_group;
    }

    public void setUser_group(Integer user_group) {
        this.user_group = user_group;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCashout() {
        return cashout;
    }

    public void setCashout(BigDecimal cashout) {
        this.cashout = cashout;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
