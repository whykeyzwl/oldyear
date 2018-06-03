package com.wishes.yearOld.Exception;

import com.wishes.yearOld.model.PaymentAliRefundCode;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-11-21
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class PaymentAliRefundException extends Exception {
    public PaymentAliRefundException() {

    }
    public PaymentAliRefundException(String msg) {
        super(msg);
    }

    public PaymentAliRefundException(PaymentAliRefundCode paymentAliRefundCode) {
        super("退款异常，code： "+paymentAliRefundCode.getCode()+"\n"+
                "msg :"+paymentAliRefundCode.getMsg()+"\n");
    }
}
