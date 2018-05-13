package com.wishes.yearOld.service;

import com.wishes.yearOld.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public interface ISaleService {

    public List<UserTradeRecord> userLastRecord(int userid, int limit);

    public UserTradeRecord getTradeRecordDetail(int id);

    public List<IncomeVO> getIncome(int userid, Date date);

    public void insertApplyCashout(ApplyCashout applyCashout);

    public List<UserRecord> queryUserRecord(UserRecord userRecord);

    public void userRecordReplenish(UserRecord userRecord);

    public UserRecord loadUserRecord(String id);

    public List<ApplyCashout> queryApplyCashout(ApplyCashout applyCashout);

    int insertUserAccount(UserAccount userAccount);

    int updateUserAccount(UserAccount userAccount);

    /**
     * 查询当前用户的可用余额、冻结金额、提现状态
     * @return
     * nowBalance  当前可用余额，不含冻结金额
     * totalCashout  总冻结金额
     * applyCashout  未完成的提现状态
     */
    public Map<String, Object> NowBalanceCashout(UserAccount userAccount);
    /**
     * 根据订单ID获取当前用户的交易信息
     * 不包含青豆交易的记录
     */
    public UserRecord loadUserRecordByOrderId(UserRecord record);
}
