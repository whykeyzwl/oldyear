package com.wishes.yearOld.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * tgod_order
 */
public class Order extends Resource{
    /**
     * column : id
     * 
     */
    private Integer id;

    /**
     * column : order_number
     * 订单号
     */
    private String orderNumber;

    /**
     * column : out_refund_no
     * 退款单号
     */
    private String outRefundNo;

    /**
     * column : trade_no
     * 第三方支付流水号
     */
    private String tradeNo;

    /**
     * column : buyer_id
     * 购买者id
     */
    private Integer buyerId;

    /**
     * column : buyer_account
     * 第三方支付账号
     */
    private String buyerAccount;

    /**
     * column : order_type
     * 购买类型（1：购买图集 2：购买活动）
     */
    private Byte orderType;

    /**
     * column : album_id
     * 图集id（购买类型为1时有值）
     */
    private Integer albumId;

    /**
     * column : album_level
     * 购买图集等级（1：一级解锁 2：套图）（购买类型为1时有值
     */
    private Byte albumLevel;

    /**
     * column : activity_id
     * 活动id（购买类型为2时有值）
     */
    private Integer activityId;

    /**
     * column : activity_rule
     * 活动规则（购买类型为2时有值）
     */
    private Integer activityRule;

    /**
     * column : activity_model
     * 活动中模特id（购买类型为2时有值）
     */
    private Integer activityModel;

    /**
     * column : trade_type
     * 第三方支付类型（1：支付宝 2：微信）
     */
    private Byte tradeType;

    /**
     * column : status
     * 订单状态（1：初始状态，等待买家付款(未支付) 2：支付中  3：交易支付完成 4：交易结束，不可退款(关闭) 5：未付款交易超时关闭(或支付失败) 6：退款中 7.退款成功 8.退款失败）
     */
    private Byte status;

    /**
     * column : total_amount
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * column : buyer_pay_amount
     * 付款金额
     */
    private BigDecimal buyerPayAmount;

    /**
     * column : receipt_amount
     * 实收金额
     */
    private BigDecimal receiptAmount;

    /**
     * column : refund_fee
     * 退款金额
     */
    private BigDecimal refundFee;

    /**
     * column : create_time
     * 交易创建时间
     */
    private Date createTime;

    /**
     * column : payment_time
     * 交易付款时间
     */
    private Date paymentTime;

    /**
     * column : finish_time
     * 交易结束时间
     */
    private Date finishTime;

    /**
     * column : refund_time
     * 交易退款时间
     */
    private Date refundTime;

    /**
     * column : notify_time
     * 接收到通知时间
     */
    private Date notifyTime;
    /**
     *支付类型(1.是vip2.图集下载3.图集解锁）
     */
    private Integer type;
    
    private Integer monthcout;
    
    public Order(Integer id, String orderNumber, String outRefundNo, String tradeNo, Integer buyerId, String buyerAccount, Byte orderType, Integer albumId, Byte albumLevel, Integer activityId, Integer activityRule, Integer activityModel, Byte tradeType, Byte status, BigDecimal totalAmount, BigDecimal buyerPayAmount, BigDecimal receiptAmount, BigDecimal refundFee, Date createTime, Date paymentTime, Date finishTime, Date refundTime, Date notifyTime,Integer type,Integer monthcout) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.outRefundNo = outRefundNo;
        this.tradeNo = tradeNo;
        this.buyerId = buyerId;
        this.buyerAccount = buyerAccount;
        this.orderType = orderType;
        this.albumId = albumId;
        this.albumLevel = albumLevel;
        this.activityId = activityId;
        this.activityRule = activityRule;
        this.activityModel = activityModel;
        this.tradeType = tradeType;
        this.status = status;
        this.totalAmount = totalAmount;
        this.buyerPayAmount = buyerPayAmount;
        this.receiptAmount = receiptAmount;
        this.refundFee = refundFee;
        this.createTime = createTime;
        this.paymentTime = paymentTime;
        this.finishTime = finishTime;
        this.refundTime = refundTime;
        this.notifyTime = notifyTime;
        this.type = type;
        this.monthcout = monthcout;
    }

    public Order(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.buyerAccount = order.getBuyerAccount();
        this.buyerPayAmount = order.getBuyerPayAmount();
        this.receiptAmount = order.getReceiptAmount();
        this.paymentTime = order.getPaymentTime();
        this.finishTime = order.getFinishTime();
        this.notifyTime = order.getNotifyTime();
        this.tradeNo = order.getTradeNo();
    }

    public Order() {
        super();
    }

    public Order(GoodsVO good) {
        this.activityId = good.getActivityId();
        this.activityModel = good.getActivityModel();
        this.activityRule = good.getActivityRule();
        this.albumId = good.getAlbumId();
        this.albumLevel = good.getAlbumLevel();
        this.orderType = good.getGoodsType();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo == null ? null : outRefundNo.trim();
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount == null ? null : buyerAccount.trim();
    }

    public Byte getOrderType() {
        return orderType;
    }

    public void setOrderType(Byte orderType) {
        this.orderType = orderType;
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

    public Byte getTradeType() {
        return tradeType;
    }

    public void setTradeType(Byte tradeType) {
        this.tradeType = tradeType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getBuyerPayAmount() {
        return buyerPayAmount;
    }

    public void setBuyerPayAmount(BigDecimal buyerPayAmount) {
        this.buyerPayAmount = buyerPayAmount;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMonthcout() {
		return monthcout;
	}

	public void setMonthcout(Integer monthcout) {
		this.monthcout = monthcout;
	}
    
    
}