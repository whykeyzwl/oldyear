package com.wishes.yearOld.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人物信息
 * Created by tmg-zwl on 2017/04/26.
 */
public class PersonalWebsite {
	private int id;//主键id
	private String userid;//用户id
	private String username;//用户名
	private String passportId;//缓存id
	private String userHerderPic;//头像
	private String userFirstPic;//封面
	private Date sysDate;//系统时间
	private int start;//起始条数
	private int pageSize;//数量
	
	private ArrayList<Photoalbuminfo> photoalbuminfoLst;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassportId() {
		return passportId;
	}
	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}
	public String getUserHerderPic() {
		return userHerderPic;
	}
	public void setUserHerderPic(String userHerderPic) {
		this.userHerderPic = userHerderPic;
	}
	public String getUserFirstPic() {
		return userFirstPic;
	}
	public void setUserFirstPic(String userFirstPic) {
		this.userFirstPic = userFirstPic;
	}
	public ArrayList<Photoalbuminfo> getPhotoalbuminfoLst() {
		return photoalbuminfoLst;
	}
	public void setPhotoalbuminfoLst(ArrayList<Photoalbuminfo> photoalbuminfoLst) {
		this.photoalbuminfoLst = photoalbuminfoLst;
	}
	public Date getSysDate() {
		return sysDate;
	}
	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
