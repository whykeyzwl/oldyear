package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
@Component("saleMapper")
public interface SaleMapper {

    List<UserTradeRecord> userLastRecord(Map<String,Object> map);

    UserTradeRecord getTradeRecordDetail(int id);

    List<IncomeVO> getIncome(Map<String,Object> map);

    void insertApplyCashout(ApplyCashout applyCashout);

    List<UserRecord> queryUserRecord(UserRecord userRecord);

    List<ApplyCashout> queryApplyCashout(ApplyCashout applyCashout);

    UserRecord loadUserRecord(String id);

    public UserRecord loadUserRecordByOrderId(UserRecord record);

    BigDecimal getAlbumIncome(@Param("userId")Integer userId, @Param("albumId")Integer albumId, @Param("begin")Date begin, @Param("end")Date end);

    List<Integer> getBuyAlbumList(Integer albumId);

    Integer getBuyAlbumCount(Integer albumId);

    int insertUserAccount(UserAccount userAccount);

    int updateUserAccount(UserAccount userAccount);
}
