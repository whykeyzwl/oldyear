package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zouyu on 2016/9/19.
 * 用户实体类
 */
public class User implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = -8046011514776405305L;
	private Integer id;//用户id
    private String loginID;//登录唯一标识
    private Byte loginType;//登录类型 1.qq 2.wechat 3.weibo
    private Integer groupId;//用户组
    private String mobile;//手机号
    private String password;//密码
    private String email;//邮箱
    private String nickName;//昵称
    private String realName;//真实姓名
    private String idNumber;//身份证号
    private String idCardBack;//身份证反面照
    private String idCardFront;//身份证正面照
    private String zhifubao;//支付宝
    private String sex;//性别
    private String face;//头像
    private String province;//省份
    private String city;//城市
    private String address;//地址
    private String postCode;//邮编
    private Integer bust;//胸围
    private Integer waist;//腰围
    private Integer hip;//臀围
    private String cup;//杯罩
    private Integer height;//身高
    private BigDecimal weight;//体重
    private String shoeSize;//鞋码
    private String star;//星座
    private String bloodType;//血型
    private String job;//职业
    private Date birthday;//出生日期
    private String hobbies;//爱好
    private String introduce;//简介
    private Integer albums;//作品数
    private Integer fans;//粉丝数
    private Integer follows;//关注人数
    private String qq;
    private String wechat;
    private String weibo;
    private Integer qingdou;//青豆数
    private String timelineCover;
    private String editorCover;
    private Integer likeCount;
    private Integer timeline;

    private String receiverName;
    private String receiverMobile;
    private String receiverCity;
    private String receiverAddress;
    private String receiverPostCode;

    private Date registerTime;
    private Date lastLoginTime;
    private Integer loginTimes;

    private Byte  status;


    private String passportId;//校验用户的随机字符串
    
    private String openId;//微信生成的id
    

    /**
     * 是否被当前用户关注
     */
    private boolean focusByCurUser;

    private int haveBuy;//是否购买过图集
    
    /**
     * 
     * 以下是新增的三个字段
     */
     /**
      * 
      * vip类型（1.vip账户）
      */
    private Integer type;
    /**
     * vip开始时间
     */
    private Date beginTime;
    
    private Date endTime;
    
    private String unionid;
    

    public int getHaveBuy() {
        return haveBuy;
    }

    public void setHaveBuy(int haveBuy) {
        this.haveBuy = haveBuy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getZhifubao() {
        return zhifubao;
    }

    public void setZhifubao(String zhifubao) {
        this.zhifubao = zhifubao;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFaceUrl() {
        if(face!=null && !face.isEmpty() && !face.startsWith("http://")&&!face.startsWith("https://"))
            return Constant.IMAGE_BASE+face;
        return face;
    }

//    public String getFace() {
//        return face;
//    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Integer getBust() {
        return bust;
    }

    public void setBust(Integer bust) {
        this.bust = bust;
    }

    public Integer getWaist() {
        return waist;
    }

    public void setWaist(Integer waist) {
        this.waist = waist;
    }

    public Integer getHip() {
        return hip;
    }

    public void setHip(Integer hip) {
        this.hip = hip;
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getAlbums() {
        return albums;
    }

    public void setAlbums(Integer albums) {
        this.albums = albums;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getFollows() {
        return follows;
    }

    public void setFollows(Integer follows) {
        this.follows = follows;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public Byte getLoginType() {
        return loginType;
    }

    public void setLoginType(Byte loginType) {
        this.loginType = loginType;
    }

    public Integer getQingdou() {
        return qingdou;
    }

    public void setQingdou(Integer qingdou) {
        this.qingdou = qingdou;
    }

    public String getTimelineCoverUrl() {
        if(timelineCover!=null && !timelineCover.isEmpty() && !timelineCover.startsWith("http://"))
            return Constant.IMAGE_BASE+timelineCover;
        return timelineCover;
    }

    public void setTimelineCover(String timelineCover) {
        this.timelineCover = timelineCover;
    }

    public String getEditorCoverUrl() {
        if(editorCover!=null && !editorCover.isEmpty() && !editorCover.startsWith("http://"))
            return Constant.IMAGE_BASE+editorCover;
        return editorCover;
    }

    public void setEditorCover(String editorCover) {
        this.editorCover = editorCover;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getTimeline() {
        return timeline;
    }

    public void setTimeline(Integer timeline) {
        this.timeline = timeline;
    }

    public boolean isFocusByCurUser() {
        return focusByCurUser;
    }

    public void setFocusByCurUser(boolean focusByCurUser) {
        this.focusByCurUser = focusByCurUser;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPostCode() {
        return receiverPostCode;
    }

    public void setReceiverPostCode(String receiverPostCode) {
        this.receiverPostCode = receiverPostCode;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getFace() {
		return face;
	}

	public String getTimelineCover() {
		return timelineCover;
	}

	public String getEditorCover() {
		return editorCover;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
    
   
	
}
