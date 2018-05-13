package com.wishes.yearOld.model;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-10-21
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
public class AlipayBizContent {

    private String body ;

    private String subject = "青豆客";

    private String out_trade_no ;

    private String total_amount;

    private String product_code = "QUICK_MSECURITY_PAY";


    public AlipayBizContent(String body,String out_trade_no, String total_amount) {
        this.body = body;
        this.out_trade_no = out_trade_no;
        this.total_amount = total_amount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
