package com.wishes.yearOld.model;

/**
 * Created by tmg-yesky on 2016/10/19.
 * 商品详情
 */
public class GoodsDetail {

    /**
     * 必填 32 商品的编号
     */
    private String goods_id;

    /**
     * 可选 32 微信支付定义的统一商品编号
     */
    private String wxpay_goods_id;

    /**
     * 必填 256 商品名称
     */
    private String goods_name;

    /**
     * 必填 商品数量
     */
    private Integer quantity;

    /**
     * 必填 商品单价，单位为分
     */
    private Integer price;

    /**
     * 可选 32 商品类目ID
     */
    private String goods_category;

    /**
     * 可选 1000 商品描述信息
     */
    private String body;

    public GoodsDetail() {
    }

    public GoodsDetail(String goods_id, String goods_name, Integer quantity, Integer price) {
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getWxpay_goods_id() {
        return wxpay_goods_id;
    }

    public void setWxpay_goods_id(String wxpay_goods_id) {
        this.wxpay_goods_id = wxpay_goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getGoods_category() {
        return goods_category;
    }

    public void setGoods_category(String goods_category) {
        this.goods_category = goods_category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
