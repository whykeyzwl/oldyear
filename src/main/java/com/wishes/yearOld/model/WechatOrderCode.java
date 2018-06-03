package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

/**
 * Created by tmg-yesky on 2017/1/16.
 */
public enum WechatOrderCode {

    SUCCESS(Constant.ORDER_FINISHED, "支付成功"),
    REFUND(Constant.ORDER_REFUNDING, "转入退款"),
    NOTPAY(Constant.ORDER_UNPAY, "未支付"),
    CLOSED(Constant.ORDER_UNPAY, "已关闭"),
    REVOKED(Constant.ORDER_CLOSED, "已撤销（刷卡支付）"),
    USERPAYING(Constant.ORDER_PAYING, "用户支付中"),
    PAYERROR(Constant.ORDER_FAILED, "支付失败");

    public static WechatOrderCode getWechatOrderCode(String code){
        WechatOrderCode prc = null;
        for(WechatOrderCode p : WechatOrderCode.values()){
            if(p.name().equals(code)) {
                prc = p;
                break;
            }
        }
        return prc;
    }

    private WechatOrderCode(byte status, String msg){
        this.status = status;
        this.msg = msg;
    }

    private byte status;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public byte getStatus() {
        return status;
    }

}
