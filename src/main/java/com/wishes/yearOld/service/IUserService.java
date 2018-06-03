package com.wishes.yearOld.service;



import com.wishes.yearOld.model.*;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * Created by zouyu on 2016/9/19.
 */
public interface IUserService {

    /**
     *  用户注册，成功返回用户信息
     */
    User register(String mobile);

    /**
     *  用户注册，成功返回用户信息
     */
    User register(User user);

    /**
     *  用户注册，成功返回用户信息(三方登录)
     */
    User register(String loginId, Byte loginType, String imgUrl, String nickName);

    /**
     *  根据用户id返回用户信息
     */
    User detail(int id,Integer curUserId);

    /**
     *  检查旧密码是否正确
     */
    boolean checkPassword(int userId, String oldPassword);
    /**
     *  修改个人密码
     */
    int changePassword(Integer userId, String newPassword);

    /**
     *  根据条件查询用户列表
     */
    List<User> queryList(UserCondition cond,Integer curUserId);

    /**
     *  更新某用户的个别信息字段
     */
    int updateValue(String itemName, String itemValue,Integer userId);

    /**
     *  修改用户手机号
     */
    int updateMobile(String mobile, Integer userId);

    /**
     *  提交用户认证申请
     */
    int certify(int userId, int userGroup, String videoUrl);

    /**
     *  查询用户认证申请的状态
     *  如多次申请则返回最近一次的状态
     */
    Integer getCertifyStatus(int userId);

    /**
     *  根据昵称查询用户
     */
    List<User> findByNickName(String nickName);

    /**
     * 根据手机号查询用户
     */

    int findByMobile(String mobile);

    /**
     *  根据唯一标识和登录类型查询用户
     */
    User findByLoginId(String LoginId, Byte type);

    /**
     *  根据passportId从缓存中获取用户对象
     */
    User loadUserFromCache(String passportId);

    /**
     *  根据passportId，将用户对象添加/更新到缓存
     */
    void syncUserToCache(String passportId, User user);

    /**
     *  从缓存中删除用户对象
     */
    void removeUserFromCache(String passportId);

    /**
     *  根据手机号和类型从缓存中获取验证码+时间戳对象
     */
    String loadSecureCodeFromCache(String mobile, int type);

    /**
     *  根据手机号和类型，将验证码+时间戳对象对象添加/更新到缓存
     */
    void syncSecureCodeToCache(String mobile, int type, String secureCode);

    /**
     *  从缓存中删除手机的某类型验证码对象
     */
    void removeSecureCodeFromCache(String mobile, int type);

    /**
     *  从缓存中删除手机对应的所有验证码对象
     */
    void removeAllSecureCodeFromCache(String mobile);

    /**
     *  查询用户是否对女神点赞
     */
    GodViewLikeVO selectIsLikeGod(GodViewLikeVO likeVO);

    /**
     *  查询用户是否点赞
     */
    boolean isLikedByCurUser(Integer likeId,Integer likeType,Integer likeById);

    /**
     *  查询用户是否关注
     */
    boolean selectIsFocus(Integer focusId,Integer focusType,Integer focusById);


    boolean selectIsCollection(Integer collectionId,Integer collectionType,Integer focusById);

    /**
     *  点赞
     */
    void giveUpGod(GodViewLikeVO likeVO);

    /**
     *  取消点赞
     */
    void giveDownGod(GodViewLikeVO likeVO);

    /**
     *  查询女神的点赞数
     */
    Integer selectGodLikeCount(Integer godUserId);

    Integer getFocusCount(Integer curUserId, Integer focusUserId);

    int saveFocusUser(Integer curUserId, Integer focusUserId);

    int saveUnfocusUser(Integer curUserId, Integer focusUserId);

    int updateReceiver(User user);

    List<ActivitySupporter> querySupporterTotalRank(Integer curUserId, Integer curUserGroup, Integer start, Integer pageSize);

    int updateAtLogin(User user);
    
    /**
     * 修改用户封面
     */
    int updateCover(User user);

    UserAccount selectUserAccount(User user);

    void addQingdou(Integer id, Integer qingdouAmount);

    public List<UsersCollection> getPhotoCollectionByUserId(Integer userId,Integer pageNo,Integer pageSize)throws Exception;
    /**
     * @date 2017-08-07
     *  用户信息
     */
	
	List<User> queryUserAllList(UserCondition cond);
    
    //修改用户vip
    int updateUserVip(User user);
    
    /**
     * @date 2017-08-16
     *  用户信息
     */
    int updateAtLoginTimes(User user);
    
    User selectUser(User user);
    
    int updateCacheCount(BigDecimal itemValue,Integer userId);

   
}
