package com.wishes.yearOld.dao;


import com.wishes.yearOld.model.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zouyu on 2016/9/19.
 */
@Component("userMapper")
public interface IUserMapper {


    /**
     *  用户注册
     */
    int register(User user);

    User findById(@Param("id")int id,@Param("curUserId")Integer curUserId);

    int updateFrom3party(User user);
    
    //修改用户vip
    int updateUserVip(User user);

    int changePassword(@Param("userId")Integer userId, @Param("newPassword")String newPassword);

    int queryCount(UserCondition cond);

    List<User> queryList(@Param("condition")UserCondition cond,@Param("curUserId")Integer curUserId);

    int updateValue(@Param("itemName") String itemName,@Param("itemValue") String itemValue,@Param("userId") Integer userId);
    int updateValue(@Param("itemName")String itemName,@Param("itemValue") Integer itemValue,@Param("userId") Integer userId);
    int updateValue(@Param("itemName")String itemName,@Param("itemValue") Date itemValue ,@Param("userId") Integer userId);
    int updateValue(@Param("itemName")String itemName,@Param("itemValue") Double itemValue,@Param("userId") Integer userId);
    int updateValueCount(@Param("itemName")String itemName,@Param("itemValue") Integer itemValue,@Param("userId") Integer userId);

    int updateMobile(@Param("mobile")String mobile, @Param("userId")Integer userId);

    String password(@Param("userId")int userId);

    int certify(@Param("userId")int userId, @Param("userGroup")int userGroup,@Param("videoUrl")String videoUrl, @Param("date")Date date);

    Integer getCertifyStatus(@Param("userId")int userId);

    List<User> findByNickName(String nickName);

    int findByMobile(String mobile);

    User findByLoginId(@Param("loginId")String loginId, @Param("loginType")Byte loginType);

    GodViewLikeVO selectIsLikeGod(GodViewLikeVO likeVO);

    UsersFocus selectIsFocus(UsersFocus focus);

    List<UsersCollection> selectIsCollection(UsersCollection focus);

    void updateGodUp(GodViewLikeVO likeVO);

    void insertGodLike(GodViewLikeVO likeVO);

    void updateGodDown(GodViewLikeVO likeVO);

    void deleteGodLike(GodViewLikeVO likeVO);

    Integer selectGodLikeCount(Integer godUserId);

    int updateTimeline(@Param("id")Integer id);

    Integer getFocusUserCount(@Param("curUserId")Integer curUserId, @Param("focusUserId")Integer focusUserId);

    int focus(@Param("curUserId")Integer curUserId, @Param("focusId")Integer focusId, @Param("focusType")Integer focusType);

    int unfocus(@Param("curUserId")Integer curUserId, @Param("focusId")Integer focusId,@Param("focusType")Integer focusType);

    int updateReceiver(User user);

    int updateAtLogin(User user);
    /**
     * 修改用户封面
     */
    int updateCover(User user);
    
    UserAccount selectUserAccount(User user);

    int getModelAlbums(Integer modelId);

    void addQingdou(@Param("userId")Integer id, @Param("qingdou")Integer qingdouAmount);

    //获取收藏图集
    public List<UsersCollection> getPhotoCollectionByUserId(@Param("userId")Integer userId,
                                                            @Param("start")Integer start,
                                                            @Param("pageSize")Integer pageSize);
    /**
     * @date 2017-08-04
     * 查询用户所有信息
      */
    List<User> queryUserAllList(UserCondition cond);
    
    int updateAtLoginTimes(User user);
    
    User selectUser(User user);
}
