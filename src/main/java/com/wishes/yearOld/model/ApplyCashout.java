package com.wishes.yearOld.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午8:33
 * To change this template use File | Settings | File Templates.
 */
public class ApplyCashout {

    private Integer id;

    private Date applyTime;

    private Byte status;//审核状态

    private Byte [] statuss;

    public Byte[] getStatuss() {
        return statuss;
    }

    public void setStatuss(Byte[] statuss) {
        this.statuss = statuss;
    }

    private Integer userId;

    private String nickname;

    private Byte userGroup;

    private String realname;

    private String accountNum;

    private String zhifubao;

    private String mobile;

    private BigDecimal cashout;

    private Byte payStatus;//支付状态

    private String failReason;//失败原因

    private Date passTime;//审批时间

    private Date payTime;//支付时间

    private Date finishTime;//支付时间

    private String batchNo;//批次号

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Byte getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Byte userGroup) {
        this.userGroup = userGroup;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getZhifubao() {
        return zhifubao;
    }

    public void setZhifubao(String zhifubao) {
        this.zhifubao = zhifubao;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getCashout() {
        return cashout;
    }

    public void setCashout(BigDecimal cashout) {
        this.cashout = cashout;
    }

    public Byte getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Date getPassTime() {
        return passTime;
    }

    public void setPassTime(Date passTime) {
        this.passTime = passTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
