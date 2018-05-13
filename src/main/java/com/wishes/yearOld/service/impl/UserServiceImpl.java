package com.wishes.yearOld.service.impl;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.EncryptUtil;
import com.wishes.yearOld.common.GroupMemcachedUtil;
import com.wishes.yearOld.common.UserClassUtil;
import com.wishes.yearOld.dao.IUserMapper;
import com.wishes.yearOld.dao.OrderMapper;
import com.wishes.yearOld.dao.PhotoAlbumDao;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.IUserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by zouyu on 2016/9/19.
 */
@Service("userService")

public class UserServiceImpl implements IUserService {

    //日志实例
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PhotoAlbumDao photoAlbumDao;



    /**
     *  查询用户
     */
    public User findById(int id,Integer curUserId){
        return userMapper.findById(id,curUserId);
    }

    /**
     *  用户注册
     */
    @Override
    public User register(String mobile) {
        User user = new User();
        user.setLoginID(mobile);
        user.setLoginType((byte)0);
        user.setMobile(mobile);
        user.setGroupId(Constant.USER_GROUP_USER);//用户第一次注册默认为普通用户
        user.setRegisterTime(new Date());
        user.setStatus((byte)0);
        userMapper.register(user);
        if(user.getId()!=null) {
            return user;
        }
        return null;
    }

    /**
     *  用户注册
     */
    @Override
    public User register(User user) {
        userMapper.register(user);
        if(user.getId()!=null) {
            return user;
        }
        return null;
    }

    @Override
    public User register(String openId, Byte type, String imgUrl, String nickName) {
        User user = new User();
        user.setLoginID(openId);
        user.setUnionid(openId);
        user.setLoginType(type);
        user.setGroupId(Constant.USER_GROUP_USER);//用户第一次注册默认为普通用户
        user.setRegisterTime(new Date());
        user.setStatus((byte)0);
        if(imgUrl!=null && !imgUrl.isEmpty()){
            user.setFace(imgUrl);
        }
        if(nickName!=null && !nickName.isEmpty()){
            List<User> userlist = userMapper.findByNickName(nickName);
            if(userlist!=null && userlist.size()>0){
                nickName += "_"+userlist.size();
            }

            user.setNickName(nickName);
        }
        userMapper.register(user);
        if(user.getId()!=null) {
            return user;
        }
        return null;
    }

    public int updateMobile(String mobile, Integer userId) {
        return userMapper.updateMobile(mobile, userId);
    }

    @Override
    public User detail(int id, Integer curUserId){
        User user = userMapper.findById(id, curUserId);
        if (userMapper.getModelAlbums(id) > 0) {
            user.setAlbums(userMapper.getModelAlbums(id));//作品数
        }
        return user;
    }

    @Override
    public int changePassword(Integer userId, String newPassword) {
        return userMapper.changePassword(userId,EncryptUtil.createDbPassword(newPassword));

    }

    @Override
    public boolean checkPassword(int userId, String oldPassword) {
        String password = userMapper.password(userId);
        return EncryptUtil.comparePasswords(password,oldPassword);
    }

    @Override
    public int certify(int userId,int userGroup, String videoUrl) {
        return userMapper.certify(userId,userGroup,videoUrl,new Date());
    }

    @Override
    public Integer getCertifyStatus(int userId) {
        return userMapper.getCertifyStatus(userId);
    }

    @Override
    public List<User> findByNickName(String nickName) {
        return userMapper.findByNickName(nickName);
    }

    @Override
    public int findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    @Override
    public User findByLoginId(String loginId, Byte type) {
        return userMapper.findByLoginId(loginId, type);
    }

    @Override
    // 查询分页列表
    public List<User> queryList(UserCondition cond,Integer curUserId) {
        int cur_page = cond.getPageNo();
        int pageSize = cond.getPageSize();
        int rowCount = userMapper.queryCount(cond);// 记录个数
        cond.setRowCount(rowCount);//
        int pageCount = rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1;// 页数
        cond.setPageCount(pageCount);
        cond.setStart(pageSize * (cur_page - 1));


        return userMapper.queryList(cond,curUserId);
    }

    @Override
    public int updateValue(String itemName, String itemValue,Integer userId) {
        int item = 0;
        String type = UserClassUtil.getType(itemName);

        try {
            if(type.equals("Integer") ){
                item = userMapper.updateValue(itemName,Integer.valueOf(itemValue),userId);
            }else if(type.equals("Date")){
                item = userMapper.updateValue(itemName,new SimpleDateFormat("yyyyMMdd").parse(itemValue),userId);
            }else if(type.equals("Double")){
                item = userMapper.updateValue(itemName,Double.valueOf(itemValue),userId);
            }else{
                item = userMapper.updateValue(itemName,itemValue,userId);
            }
        }catch(Exception e){
            logger.error("用户修改信息"+itemName+"失败",e);
        }
        return item;
    }

    @Override
    public User loadUserFromCache(String passportId){
        return (User) GroupMemcachedUtil.get("tgod", passportId);
    }

    @Override
    public void syncUserToCache(String passportId, User user){
        Integer _have = orderMapper.selectHaveBuy(user.getId());
        if(_have > 0){
            user.setHaveBuy(1);
        }else{
            user.setHaveBuy(0);
        }
        GroupMemcachedUtil.store("tgod", passportId, user);
    }

    @Override
    public void removeUserFromCache(String passportId){
        GroupMemcachedUtil.remove("tgod", passportId);
    }

    @Override
    public String loadSecureCodeFromCache(String mobile, int type){
        String value =  (String)GroupMemcachedUtil.get("tgod-secureCode", mobile+"-"+type);
        if(value!=null) {
            String[] items = value.split("-");
            if(items!=null && items.length==2){
                long time = Long.parseLong(items[1]);
                if(System.currentTimeMillis()-time <= Constant.SECURE_CODE_ALIVE){
                    return items[0];
                }
            }
        }
        return null;
    }

    @Override
    public void syncSecureCodeToCache(String mobile,int type, String secureCode){
        String value = secureCode+"-"+System.currentTimeMillis();
        GroupMemcachedUtil.store("tgod-secureCode", mobile+"-"+type, value);
    }

    @Override
    public void removeSecureCodeFromCache(String mobile,int type){
        GroupMemcachedUtil.remove("tgod-secureCode", mobile+"-"+type);
    }

    @Override
    public void removeAllSecureCodeFromCache(String mobile){
        for(int type = 1; type < 5; type++)
            removeSecureCodeFromCache(mobile,type);
    }

    @Override
    public GodViewLikeVO selectIsLikeGod(GodViewLikeVO likeVO) {
        return userMapper.selectIsLikeGod(likeVO);
    }

    public boolean isLikedByCurUser(Integer likeId,Integer likeType,Integer likeById){
        if(likeById!=null){
            GodViewLikeVO likeVO = new GodViewLikeVO();
            likeVO.setUserId(likeById);
            likeVO.setLikeId(likeId);
            likeVO.setLikeType(likeType);
            //查询是否已赞
            GodViewLikeVO vo = selectIsLikeGod(likeVO);
            if (vo != null)
                return true;
        }

        return false;
    }

    public boolean selectIsFocus(Integer focusId, Integer focusType, Integer focusById) {
        if (focusById != null) {
            UsersFocus focus_ = new UsersFocus();
            focus_.setUserId(focusById);
            focus_.setFocusId(focusId);
            focus_.setFocusType((byte) 0);
            //查询是否关注
            UsersFocus focus = userMapper.selectIsFocus(focus_);
            if (focus != null)
                return true;
        }

        return false;
    }

    public boolean selectIsCollection(Integer collectionId,Integer collectionType,Integer focusById) {
        if (focusById != null) {
            UsersCollection focus_ = new UsersCollection();
            focus_.setUserId(focusById);
            focus_.setCollectionId(collectionId);
            focus_.setCollectionType((byte)collectionType.intValue());
            //查询是否关注
            List<UsersCollection> focus = userMapper.selectIsCollection(focus_);
            
            if (focus != null && focus.size()>0){
                return true;
            }else{
            	return false;
            }
        }

        return false;
    }

    /**
     * 点赞女神
     * @param likeVO
     */
    @Override
    public void giveUpGod(GodViewLikeVO likeVO) {

        userMapper.insertGodLike(likeVO);
        userMapper.updateGodUp(likeVO);
    }

    /**
     * 取消点赞女神
     * @param likeVO
     */
    @Override
    public void giveDownGod(GodViewLikeVO likeVO) {

        userMapper.deleteGodLike(likeVO);
        userMapper.updateGodDown(likeVO);
    }

    @Override
    public Integer selectGodLikeCount(Integer godUserId) {
        return userMapper.selectGodLikeCount(godUserId);
    }

    @Override
    public Integer getFocusCount(Integer curUserId, Integer focusUserId){return userMapper.getFocusUserCount(curUserId,focusUserId);}

    @Override
    public int saveFocusUser(Integer curUserId, Integer focusUserId) {
        userMapper.focus(curUserId,focusUserId,Constant.FOCUS_USER);
        User curUser = userMapper.findById(curUserId,null);
        userMapper.updateValueCount("follows",curUser.getFollows()+1,curUserId);
        User focusUser = userMapper.findById(focusUserId,null);
        userMapper.updateValueCount("fans",focusUser.getFans()+1,focusUserId);
        return 1;
    };

    @Override
    public int saveUnfocusUser(Integer curUserId, Integer focusUserId){
        userMapper.unfocus(curUserId,focusUserId,Constant.FOCUS_USER);
        User curUser = userMapper.findById(curUserId,null);
        userMapper.updateValueCount("follows",curUser.getFollows()-1,curUserId);
        User focusUser = userMapper.findById(focusUserId,null);
        userMapper.updateValueCount("fans",focusUser.getFans()-1,focusUserId);
        return 1;
    }

    @Override
    public int updateReceiver(User user) { return userMapper.updateReceiver(user); }

    @Override
    public int updateAtLogin(User user) { return userMapper.updateAtLogin(user); }

    @Override
    public List<ActivitySupporter> querySupporterTotalRank(Integer curUserId, Integer curUserGroup, Integer pageNo, Integer pageSize){
        if(pageNo==null)
            pageNo = Constant.DEF_PAGE_NUM;
        if(pageSize==null)
            pageSize = Constant.DEF_PAGE_SIZE;

        return orderMapper.querySupporterTotalRank(curUserId, curUserGroup,(pageNo-1)*pageSize, pageSize);
    }

    @Override
    public UserAccount selectUserAccount(User user) {
        return  userMapper.selectUserAccount(user);

    }

    @Override
    public void addQingdou(Integer id, Integer qingdouAmount) {
        userMapper.addQingdou(id,qingdouAmount);
    }


    @Override
    public List<UsersCollection> getPhotoCollectionByUserId(Integer userId, Integer pageNo,
                                                            Integer pageSize) throws Exception{
        if (pageNo < 0){
            throw new Exception("参数错误");
        }
        List<UsersCollection> lists = userMapper.getPhotoCollectionByUserId(userId,(pageNo-1)*pageSize,pageSize);
        for(UsersCollection list:lists){
            if(StringUtils.isTrimEmpty(list.getCover())){
                List<Photo> photos = photoAlbumDao.photos(list.getAlbumId());
                if(photos != null && photos.size() > 0) {
                    list.setCover(photos.get(0).getFullPath());
                }
            }
        }
        return lists;
    }
 /**
  * 更改用户封面
  */
	@Override
	public int updateCover(User user) {
		
		return userMapper.updateCover(user);
	}
	 /**
     * @date 2017-08-07
     *  用户信息
     */
    @Override
    public List<User> queryUserAllList(UserCondition cond) {
	
	return userMapper.queryUserAllList(cond);
    }
    /**
     * @date 2017-08-07
     * 修改用户vip
     */
    @Override
    public int updateUserVip(User user) {
	
	return userMapper.updateUserVip(user);
   }

	@Override
	public int updateAtLoginTimes(User user) {
		
		return userMapper.updateAtLoginTimes(user);
	}

	@Override
	public User selectUser(User user) {
		
		return userMapper.selectUser(user);
	}
}
