package com.wishes.yearOld.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * tgod_records_cashout
 */
public class RecordsCashout {
    /**
     * column : id
     * 
     */
    private Integer id;

    /**
     * column : batch_no
     * 批次号
     */
    private String batchNo;

    /**
     * column : create_time
     * 创建时间
     */
    private Date createTime;

    /**
     * column : finish_time
     * 处理完成时间
     */
    private Date finishTime;

    /**
     * column : batch_status
     * 批次状态  1:初始状态 2:提交成功 3:提交失败 4:支付宝处理中（转账申请提交成功，但是支付宝正在处理，例如账户余额不足，需要在24小时内充值）
     */
    private Byte batchStatus;

    /**
     * column : batch_fee
     * 批次金额（精确到两位小数）
     */
    private BigDecimal batchFee;

    /**
     * column : user_id
     * 用户id
     */
    private Integer userId;

    /**
     * column : user_account
     * 用户支付宝账号
     */
    private String userAccount;

    /**
     * column : user_name
     * 用户真实姓名
     */
    private String userName;

    /**
     * column : user_status
     * 1:初始状态 2：转账成功 3：转账失败
     */
    private Byte userStatus;

    /**
     * column : fail_reason
     * 用户转账失败原因
     */
    private String failReason;

    /**
     * column : user_serial_number
     * 
     */
    private String userSerialNumber;

    public RecordsCashout(Integer id, String batchNo, Date createTime, Date finishTime, Byte batchStatus, BigDecimal batchFee, Integer userId, String userAccount, String userName, Byte userStatus, String failReason, String userSerialNumber) {
        this.id = id;
        this.batchNo = batchNo;
        this.createTime = createTime;
        this.finishTime = finishTime;
        this.batchStatus = batchStatus;
        this.batchFee = batchFee;
        this.userId = userId;
        this.userAccount = userAccount;
        this.userName = userName;
        this.userStatus = userStatus;
        this.failReason = failReason;
        this.userSerialNumber = userSerialNumber;
    }

    public RecordsCashout() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Byte getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(Byte batchStatus) {
        this.batchStatus = batchStatus;
    }

    public BigDecimal getBatchFee() {
        return batchFee;
    }

    public void setBatchFee(BigDecimal batchFee) {
        this.batchFee = batchFee;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Byte getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason == null ? null : failReason.trim();
    }

    public String getUserSerialNumber() {
        return userSerialNumber;
    }

    public void setUserSerialNumber(String userSerialNumber) {
        this.userSerialNumber = userSerialNumber == null ? null : userSerialNumber.trim();
    }
}