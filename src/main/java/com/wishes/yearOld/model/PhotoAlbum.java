package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 套图
 * Created by tmg-yesky on 2016/9/19.
 */
public class PhotoAlbum extends Resource {

    /**
     */
    private Integer id;

    /**
     * 套图标题
     */
    private String title;

    /**
     * 套图简介
     */
    private String description;

    /**
     * 模特ID
     */
    private Integer modelId;

    /**
     * 模特名称
     */
    private String modelName;

    /**
     * 模特头像
     */
    private String modelFace;
    /**
     * 模特作品数
     */
    private Integer modelAlbums;

    /**
     * 写真师ID
     */
    private Integer photoGrapherId;

    /**
     * 写真师名称
     */
    private String photoGrapherName;
    /**
     * 写真师头像
     */
    private String photoGrapherFace;
    /**
     * 写真师作品数
     */
    private Integer photoGrapherAlbums;

    /**
     * 零售价1: 购买一级解锁
     */
    private BigDecimal price3;

    /**
     * 零售价2: 一级解锁升级到二级解锁
     */
    private BigDecimal downprice;

    /**
     * 零售价3: 全套购买
     */
    private BigDecimal viewprice;

    private BigDecimal realPrice;

    /**
     * 模特分润比例
     */
    private BigDecimal modelProfitRate;

    /**
     * 写真师分润比例
     */
    private BigDecimal photoGrapherProfitRate;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 图片张数
     */
    private Integer pages;

    /**
     * 压缩包地址
     */
    private String ziplink;

    /**
     * 销售量
     */
    private Integer sales;

    /**
     * 相关活动ID
     */
    private Integer activityId;

    /**
     * 作品状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 更新时间
     */
    private Date lastUpdateTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 审核人
     */
    private String reviewer;

    /**
     * 限时免费
     */
    private Integer limitFree;

    /**
     * 推荐 1.是 0.否
     */
    private Byte recommend;

    /**
     * 青豆价格
     */
    private Integer qingdou;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 点击量
     */
    private Integer viewCount;

    private Integer type;

    private Integer userHavebuy;//当前用户是否购买过任意图集

    private String cert;//女神认证信息
    
    private Integer controlLevel;//控制显示级别
    
    private String positioninfo;//发表位置
    
    private int IsQdk;//是否青豆客发布（0.不是青豆客发布1.是青豆客发布）

    public Integer getUserHavebuy() {
        return userHavebuy;
    }

    public void setUserHavebuy(Integer userHavebuy) {
        this.userHavebuy = userHavebuy;
    }

    private String unLockLV1;

    private Date limitFreeTime;

    private boolean isLikedByCurUser; // 是否已被当前登陆用户点赞

    private boolean isCollection; // 是否已被当前登陆用户收藏

    private boolean isCommentByCurUser; // 是否已被当前登陆用户点赞

    public boolean isCommentByCurUser() {
        return isCommentByCurUser;
    }

    public void setCommentByCurUser(boolean isCommentByCurUser) {
        this.isCommentByCurUser = isCommentByCurUser;
    }

    private boolean focusModelByCurUser; // 是否已被当前登陆用户关注
    private boolean focusPhotographerByCurUser; // 是否已被当前登陆用户关注

    private Double star; //星级

    private AlbumPermission permission;

    private List<Photo> photos;

    private List<Photo> levelOnePhotos;

    private List<Tag> tags;

    private UserAlbumLog userAlbumLog;

    private Integer userId;//当前登录用户ID

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserAlbumLog getUserAlbumLog() {
        return userAlbumLog;
    }

    public void setUserAlbumLog(UserAlbumLog userAlbumLog) {
        this.userAlbumLog = userAlbumLog;
    }

    /**
     * 图集收入统计
     */
    private AlbumIncome albumIncome;

    private int userBeanNums;//当前用户青豆数量

    private int collectionNums;//图集被收藏数量

    private int shareNums;//图集被分享次数

    private int buy;//图集是否被购买

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public String getCover() {
        return cover;
    }

    public String getZiplink() {
        return ziplink;
    }

    public int getUserBeanNums() {
        return userBeanNums;
    }

    public void setUserBeanNums(int userBeanNums) {
        this.userBeanNums = userBeanNums;
    }

    public int getCollectionNums() {
        return collectionNums;
    }

    public void setCollectionNums(int collectionNums) {
        this.collectionNums = collectionNums;
    }

    public int getShareNums() {
        return shareNums;
    }

    public void setShareNums(int shareNums) {
        this.shareNums = shareNums;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getPhotoGrapherId() {
        return photoGrapherId;
    }

    public void setPhotoGrapherId(Integer photoGrapherId) {
        this.photoGrapherId = photoGrapherId;
    }

    public String getPhotoGrapherName() {
        return photoGrapherName;
    }

    public void setPhotoGrapherName(String photoGrapherName) {
        this.photoGrapherName = photoGrapherName;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public BigDecimal getDownprice() {
        return downprice;
    }

    public void setDownprice(BigDecimal downprice) {
        this.downprice = downprice;
    }

    public BigDecimal getModelProfitRate() {
        return modelProfitRate;
    }

    public void setModelProfitRate(BigDecimal modelProfitRate) {
        this.modelProfitRate = modelProfitRate;
    }

    public BigDecimal getPhotoGrapherProfitRate() {
        return photoGrapherProfitRate;
    }

    public void setPhotoGrapherProfitRate(BigDecimal photoGrapherProfitRate) {
        this.photoGrapherProfitRate = photoGrapherProfitRate;
    }

    public BigDecimal getViewprice() {
        return viewprice;
    }

    public void setViewprice(BigDecimal viewprice) {
        this.viewprice = viewprice;
    }

    public String getCoverUrl() {
        if (cover!=null && !cover.isEmpty() && cover.startsWith("/"))
            cover = cover.substring(1);
        if(cover!=null && !cover.isEmpty() && !cover.startsWith("http://")) {
            return Constant.IMAGE_BASE + cover;
        }
        return cover;
    }

    public String getModelFaceUrl() {
        if (modelFace != null && modelFace.startsWith("/"))
            modelFace = modelFace.substring(1);
        if(modelFace!=null && !modelFace.isEmpty() && !modelFace.startsWith("http://")) {
            return Constant.IMAGE_BASE + modelFace;
        }
        return modelFace;
    }

//    public String getCover() {
//        return cover;
//    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    //不使用get方法， 在json字符串中屏蔽压缩包信息
    public String fetchZiplinkUrl() {
        if(ziplink!=null && !ziplink.isEmpty() && !ziplink.startsWith("http://"))
            return Constant.IMAGE_BASE+ziplink;
        return ziplink;
    }

//    public String getZiplink() {
//        return ziplink;
//    }

    public void setZiplink(String ziplink) {
        this.ziplink = ziplink;
    }

    public String getSalesStr() {
        if (sales == null) {
            return null;
        }
        if (sales < 10000)
            return sales.toString();
        else {
            return String.format("%.1f万", (float) sales / 10000.0);
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Integer getLimitFree() {
        if((this.viewprice != null && this.viewprice == new BigDecimal(0.00)) ||
                (this.limitFree != null && this.limitFree == 1)){
            this.limitFree = 1;
        }
        return limitFree;
    }

    public void setLimitFree(Integer limitFree) {
        this.limitFree = limitFree;
    }

    public Integer getQingdou() {
        return qingdou;
    }

    public void setQingdou(Integer qingdou) {
        this.qingdou = qingdou;
    }

    public String getViewCountStr() {
        if (viewCount == null) {
            return null;
        }
        if (viewCount < 10000)
            return viewCount.toString();
        else {
            return String.format("%.1f万", (float) viewCount / 10000.0);
        }
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getLikeCountStr() {
        if (likeCount == null) {
            return null;
        }
        if (likeCount < 10000)
            return likeCount.toString();
        else {
            return String.format("%.1f万", (float) likeCount / 10000.0);
        }
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Byte getRecommend() {
        return recommend;
    }

    public void setRecommend(Byte recommend) {
        this.recommend = recommend;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void incViewCount() {
        viewCount++;
    }

    public Integer getSales() {
        return sales;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public boolean isLikedByCurUser() {
        return isLikedByCurUser;
    }

    public void setLikedByCurUser(boolean likedByCurUser) {
        isLikedByCurUser = likedByCurUser;
    }

    public AlbumPermission getPermission() {
        return permission;
    }

    public void setPermission(AlbumPermission permission) {
        this.permission = permission;
    }

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    public AlbumIncome getAlbumIncome() {
        return albumIncome;
    }

    public void setAlbumIncome(AlbumIncome albumIncome) {
        this.albumIncome = albumIncome;
    }

    public String getModelFace() {
        return modelFace;
    }

    public void setModelFace(String modelFace) {
        this.modelFace = modelFace;
    }

    public Integer getModelAlbums() {
        return modelAlbums;
    }

    public void setModelAlbums(Integer modelAlbums) {
        this.modelAlbums = modelAlbums;
    }

    public String getPhotoGrapherFace() {
        return photoGrapherFace;
    }

    public void setPhotoGrapherFace(String photoGrapherFace) {
        this.photoGrapherFace = photoGrapherFace;
    }

    public Integer getPhotoGrapherAlbums() {
        return photoGrapherAlbums;
    }

    public void setPhotoGrapherAlbums(Integer photoGrapherAlbums) {
        this.photoGrapherAlbums = photoGrapherAlbums;
    }

    public boolean isFocusModelByCurUser() {
        return focusModelByCurUser;
    }

    public void setFocusModelByCurUser(boolean focusModelByCurUser) {
        this.focusModelByCurUser = focusModelByCurUser;
    }

    public boolean isFocusPhotographerByCurUser() {
        return focusPhotographerByCurUser;
    }

    public void setFocusPhotographerByCurUser(boolean focusPhotographerByCurUser) {
        this.focusPhotographerByCurUser = focusPhotographerByCurUser;
    }

    public String getStar() {
        return String.format("%.1f",star);
    }

    public void setStar(Double star) {
        this.star = star==null?0:star;
    }

    public List<Photo> getLevelOnePhotos() {
        return levelOnePhotos;
    }

    public void setLevelOnePhotos(List<Photo> levelOnePhotos) {
        this.levelOnePhotos = levelOnePhotos;
    }

    public String getUnLockLV1() {
        return unLockLV1;
    }

    public void setUnLockLV1(String unLockLV1) {
        this.unLockLV1 = unLockLV1;
    }

    public Date getLimitFreeTime() {
        return limitFreeTime;
    }

    public void setLimitFreeTime(Date limitFreeTime) {
        this.limitFreeTime = limitFreeTime;
    }

    public String getCert() {
        if(StringUtils.isEmpty(cert)){
            cert = "";
        }
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

	public Integer getControlLevel() {
		return controlLevel;
	}

	public void setControlLevel(Integer controlLevel) {
		this.controlLevel = controlLevel;
	}

	public String getPositioninfo() {
		return positioninfo;
	}

	public void setPositioninfo(String positioninfo) {
		this.positioninfo = positioninfo;
	}

	public int getIsQdk() {
		return IsQdk;
	}

	public void setIsQdk(int isQdk) {
		IsQdk = isQdk;
	}

    
    
	
	
}
