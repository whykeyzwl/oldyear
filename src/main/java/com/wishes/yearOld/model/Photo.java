package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

/**
 * 图片
 * Created by tmg-yesky on 2016/9/19.
 */
public class Photo extends PhotoParam{

    /**
     * 图片ID
     */
    private Integer id;

    /**
     * 套图ID
     */
    private Integer albumid;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 解锁等级
     */
    private Integer viewtype;

    /**
     * 图片地址
     */
    private String path;

    /**
     * 图片标题
     */
    private String title;

    /**
     * 图片描述
     */
    private String description;

    /**
     * 文件大小
     */
    private String filesize;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 图片宽度
     */
    private Integer weight;
    
    private String detailinfo;

    private Integer userLevel = 0;  // 用户对于当前图集的购买级别

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

  

    public Integer getAlbumid() {
		return albumid;
	}

	public void setAlbumid(Integer albumid) {
		this.albumid = albumid;
	}

	public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getViewtype() {
        return viewtype;
    }

    public void setViewtype(Integer viewtype) {
        this.viewtype = viewtype;
    }

    public String getFullPath() {
        if(path!=null && !path.isEmpty() && path.startsWith("/")){
            path = path.substring(1);
        }
        if(path!=null && !path.isEmpty() && !path.startsWith("http://"))
            return Constant.IMAGE_BASE+path;
        return path;
    }

   public String getPaths() {
       if (!path.isEmpty() && path.startsWith("/"))
           path = path.substring(1);
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    
    
    
    public String getDetailinfo() {
		return detailinfo;
	}

	public void setDetailinfo(String detailinfo) {
		this.detailinfo = detailinfo;
	}

	public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    /**
     *  返回用户是否可见
     * @return
     */
    public  boolean isUserEnable() {
    	if(viewtype==null){
    		viewtype=1;
    	}
        return userLevel >= viewtype;
    }
}
