package com.wishes.yearOld.model;


import java.math.BigDecimal;
import java.util.Date;



public class TgodAlbumcountinfo {
    private Integer id;//图集id

    private Integer viewCount;//浏览次数

    private Integer sales;//销售量

    private Integer likeCount;//点赞次数

    private BigDecimal profittotal;//总利润

    private BigDecimal modelprofittotal;//模特总利润

    private BigDecimal photographerprofittotal;//写真师总利润

    private BigDecimal siteprofittotal;//网站总利润

    private BigDecimal organprofittotal;//合作方总利润
    
    private BigDecimal amount;
    
    private BigDecimal cashout;
    
    private BigDecimal balance;
    
    private Integer qingdouAmount;
    
    private Integer albumCount;
    
    private Integer albumAmount;
    
    private Integer dynamicAmount;
    
    private Integer downcount;//下载次数
    
    private Integer clickcount;//点击量
    
    private Integer totalcost;//总成本
    
    private String title;//标题
    
    private Byte status;//作品状态
    
    private Integer modelid;//模特ID

    private String modelname;//模特名称

    private Integer photographerid;//写真师ID

    private String photographername;//写真师名称
    
    private BigDecimal downtotalPrice;//下载总额
    
    private BigDecimal viewtotalPrice;//浏览总额
    
    private Integer dynamicCount;//总动态数
    
    private Integer redPassCount;//红包通过动态数
    
    private Integer redNoPassCount;//红包未通过动态数
    private Integer dynamicPassCount;//动态通过数
    private Integer dynamicNoPassCount;//动态没有通过数
    
    private Byte type;//默认1：1，平台发布，2，模特发布动态，3，红包照片
    
    private String nickname;
    
    private String fromTime;
    
    private String toTime;
    
    private Date createtime;
    
    private Integer albumid;
    
    private String year;
    
    private String month;
    
    private Integer qingdouAmountconsum;
    
    private String publishTime;

	public TgodAlbumcountinfo(Integer id, Integer viewCount, Integer sales,
			Integer likeCount, BigDecimal profittotal,BigDecimal modelprofittotal, 
			BigDecimal photographerprofittotal, BigDecimal siteprofittotal,
			BigDecimal organprofittotal,Integer downcount, Integer clickcount, Integer totalcost) {
		super();
		this.id = id;
		this.viewCount = viewCount;
		this.sales = sales;
		this.likeCount = likeCount;
		this.profittotal = profittotal;
		this.modelprofittotal = modelprofittotal;
		this.photographerprofittotal = photographerprofittotal;
		this.siteprofittotal = siteprofittotal;
		this.organprofittotal = organprofittotal;
		this.downcount = downcount;
		this.clickcount = clickcount;
		this.totalcost = totalcost;
	}

	public Integer getDynamicCount() {
		return dynamicCount;
	}

	public void setDynamicCount(Integer dynamicCount) {
		this.dynamicCount = dynamicCount;
	}

	public Integer getRedPassCount() {
		return redPassCount;
	}

	public void setRedPassCount(Integer redPassCount) {
		this.redPassCount = redPassCount;
	}

	public Integer getRedNoPassCount() {
		return redNoPassCount;
	}

	public void setRedNoPassCount(Integer redNoPassCount) {
		this.redNoPassCount = redNoPassCount;
	}

	public Integer getDynamicPassCount() {
		return dynamicPassCount;
	}

	public void setDynamicPassCount(Integer dynamicPassCount) {
		this.dynamicPassCount = dynamicPassCount;
	}

	public Integer getDynamicNoPassCount() {
		return dynamicNoPassCount;
	}

	public void setDynamicNoPassCount(Integer dynamicNoPassCount) {
		this.dynamicNoPassCount = dynamicNoPassCount;
	}

	public BigDecimal getDowntotalPrice() {
		return downtotalPrice;
	}

	public void setDowntotalPrice(BigDecimal downtotalPrice) {
		this.downtotalPrice = downtotalPrice;
	}
    
	
	
	public BigDecimal getViewtotalPrice() {
		return viewtotalPrice;
	}

	public void setViewtotalPrice(BigDecimal viewtotalPrice) {
		this.viewtotalPrice = viewtotalPrice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getModelid() {
		return modelid;
	}

	public void setModelid(Integer modelid) {
		this.modelid = modelid;
	}

	public String getModelname() {
		return modelname;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public Integer getPhotographerid() {
		return photographerid;
	}

	public void setPhotographerid(Integer photographerid) {
		this.photographerid = photographerid;
	}

	public String getPhotographername() {
		return photographername;
	}

	public void setPhotographername(String photographername) {
		this.photographername = photographername;
	}

	public TgodAlbumcountinfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public BigDecimal getProfittotal() {
        return profittotal;
    }

    public void setProfittotal(BigDecimal profittotal) {
        this.profittotal = profittotal;
    }

    public BigDecimal getModelprofittotal() {
        return modelprofittotal;
    }

    public void setModelprofittotal(BigDecimal modelprofittotal) {
        this.modelprofittotal = modelprofittotal;
    }

    public BigDecimal getPhotographerprofittotal() {
        return photographerprofittotal;
    }

    public void setPhotographerprofittotal(BigDecimal photographerprofittotal) {
        this.photographerprofittotal = photographerprofittotal;
    }

    public BigDecimal getSiteprofittotal() {
        return siteprofittotal;
    }

    public void setSiteprofittotal(BigDecimal siteprofittotal) {
        this.siteprofittotal = siteprofittotal;
    }

    public BigDecimal getOrganprofittotal() {
        return organprofittotal;
    }

    public void setOrganprofittotal(BigDecimal organprofittotal) {
        this.organprofittotal = organprofittotal;
    }

    public Integer getDowncount() {
        return downcount;
    }

    public void setDowncount(Integer downcount) {
        this.downcount = downcount;
    }
    
	public Integer getClickcount() {
		return clickcount;
	}

	public void setClickcount(Integer clickcount) {
		this.clickcount = clickcount;
	}

	public Integer getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(Integer totalcost) {
		this.totalcost = totalcost;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCashout() {
		return cashout;
	}

	public void setCashout(BigDecimal cashout) {
		this.cashout = cashout;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getQingdouAmount() {
		return qingdouAmount;
	}

	public void setQingdouAmount(Integer qingdouAmount) {
		this.qingdouAmount = qingdouAmount;
	}

	public Integer getAlbumCount() {
		return albumCount;
	}

	public void setAlbumCount(Integer albumCount) {
		this.albumCount = albumCount;
	}

	public Integer getAlbumAmount() {
		return albumAmount;
	}

	public void setAlbumAmount(Integer albumAmount) {
		this.albumAmount = albumAmount;
	}

	public Integer getDynamicAmount() {
		return dynamicAmount;
	}

	public void setDynamicAmount(Integer dynamicAmount) {
		this.dynamicAmount = dynamicAmount;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
    
	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getAlbumid() {
		return albumid;
	}

	public void setAlbumid(Integer albumid) {
		this.albumid = albumid;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getQingdouAmountconsum() {
		return qingdouAmountconsum;
	}

	public void setQingdouAmountconsum(Integer qingdouAmountconsum) {
		this.qingdouAmountconsum = qingdouAmountconsum;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
}