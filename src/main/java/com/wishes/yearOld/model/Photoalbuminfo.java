package com.wishes.yearOld.model;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * 图集信息
 * Created by tmg-zwl on 2017/04/26.
 */
public class Photoalbuminfo extends Resource {
private int photoalbumId;//图集id
private Date time;//时间
private int sum;//图片数量
private int IsQdk;//是否青豆客发布
private int like_count;//点赞数
private int viewCount;
private int modelId;
private int permitType;//浏览权限5是有权限浏览
private String modelName;
private String modelFace;
private String saleranking;//销售排名百分比
private String positioninfo;//位置
private String is_like;//是否已点赞1：是2：否
private String title;//标题
private String description;//文字描述内容
private ArrayList<Piclist> piclist;
private Integer userId;
private BigDecimal viewprice;

public int getPhotoalbumId() {
	return photoalbumId;
}
public void setPhotoalbumId(int photoalbumId) {
	this.photoalbumId = photoalbumId;
}
public Date getTime() {
	return time;
}
public void setTime(Date time) {
	this.time = time;
}
public int getSum() {
	return sum;
}
public void setSum(int sum) {
	this.sum = sum;
}
public int getIsQdk() {
	return IsQdk;
}
public void setIsQdk(int isQdk) {
	IsQdk = isQdk;
}
public int getLike_count() {
	return like_count;
}
public void setLike_count(int like_count) {
	this.like_count = like_count;
}
public String getSaleranking() {
	return saleranking;
}
public void setSaleranking(String saleranking) {
	this.saleranking = saleranking;
}
public String getPositioninfo() {
	return positioninfo;
}
public void setPositioninfo(String positioninfo) {
	this.positioninfo = positioninfo;
}
public String getIs_like() {
	return is_like;
}
public void setIs_like(String is_like) {
	this.is_like = is_like;
}
public ArrayList<Piclist> getPiclist() {
	return piclist;
}
public void setPiclist(ArrayList<Piclist> piclist) {
	this.piclist = piclist;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getModelId() {
	return modelId;
}
public void setModelId(int modelId) {
	this.modelId = modelId;
}
public String getModelName() {
	return modelName;
}
public void setModelName(String modelName) {
	this.modelName = modelName;
}
public String getModelFace() {
	return modelFace;
}
public void setModelFace(String modelFace) {
	this.modelFace = modelFace;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public int getViewCount() {
	return viewCount;
}
public void setViewCount(int viewCount) {
	this.viewCount = viewCount;
}
public int getPermitType() {
	return permitType;
}
public void setPermitType(int permitType) {
	this.permitType = permitType;
}


public Integer getUserId() {
	return userId;
}
public void setUserId(Integer userId) {
	this.userId = userId;
}
public BigDecimal getViewprice() {
	return viewprice;
}
public void setViewprice(BigDecimal viewprice) {
	this.viewprice = viewprice;
}

}
