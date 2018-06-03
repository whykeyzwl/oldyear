package com.wishes.yearOld.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20 0020.
 */
public class GodMoments extends Resource {
    private int id;//女神ID
    private String name;//女神昵称
    private String icon;//女神头像
    private String audited;//女神认证方式
    private int isCare;//当前用户是否关注
    private int controlLevel;//控制级别

    private List<PhotoAlbum> albumList;//图集列表

    private int userID;//当前用户ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAudited() {
        return audited;
    }

    public void setAudited(String audited) {
        this.audited = audited;
    }

    public int getIsCare() {
        return isCare;
    }

    public void setIsCare(int isCare) {
        this.isCare = isCare;
    }

    public List<PhotoAlbum> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<PhotoAlbum> albumList) {
        this.albumList = albumList;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

	public int getControlLevel() {
		return controlLevel;
	}

	public void setControlLevel(int controlLevel) {
		this.controlLevel = controlLevel;
	}
    
}
