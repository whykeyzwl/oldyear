package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.StringUtils;
import com.wishes.yearOld.dao.OrderMapper;
import com.wishes.yearOld.dao.PhotoAlbumDao;
import com.wishes.yearOld.dao.SaleMapper;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.ISaleService;
import com.wishes.yearOld.service.PhotoAlbumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */
@Service("saleService")
public class SaleServiceImpl implements ISaleService {

    @Resource
    private SaleMapper saleMapper;

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    PhotoAlbumService photoAlbumService;

    @Autowired
    private PhotoAlbumDao photoAlbumDao;

    /**
     * 获取用户最近消费记录
     * @param userid    用户id
     * @param limit     限制条数
     * @return
     */
    @Override
    public List<UserTradeRecord> userLastRecord(int userid, int limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", userid);
        params.put("begin", 0);
        params.put("end", limit*1);
        return saleMapper.userLastRecord(params);
    }

    /**
     * 获取用户消费记录详情
     * @param id
     * @return
     */
    @Override
    public UserTradeRecord getTradeRecordDetail(int id) {
        return saleMapper.getTradeRecordDetail(id);
    }

    /**
     * 获取用户的收入
     * @param userid
     * @param date
     * @return
     */
    @Override
    public List<IncomeVO> getIncome(int userid, Date date) {
        Map<String,Object> params = new HashMap<String, Object>() ;
        params.put("userid", userid);
        params.put("date", date);
        return saleMapper.getIncome( params);
    }

    /**
     * 新增提取现金
     * @param applyCashout
     */
    @Override
    public void insertApplyCashout(ApplyCashout applyCashout) {
        saleMapper.insertApplyCashout(applyCashout);
    }

    @Override
    public List<UserRecord> queryUserRecord(UserRecord userRecord) {
        return saleMapper.queryUserRecord(userRecord);
    }

    @Override
    public void userRecordReplenish(UserRecord userRecord) {
        if (userRecord.getTradeType() == Constant.USER_RECORD_QINGDOU
                && userRecord.getAlbumId() != null && userRecord.getAlbumId().intValue() > 0) {
            PhotoAlbum photoAlbum = photoAlbumService.loadPhotoAlbum(userRecord.getAlbumId());
            if (photoAlbum!=null&&
                    StringUtils.isEmpty(photoAlbum.getCoverUrl())) {
                Photo photo = photoAlbumDao.photoinfo(photoAlbum.getId(),0);
                if(photo!=null)
                    photoAlbum.setCover(photo.getFullPath());

            }
            userRecord.setPhotoAlbum(photoAlbum);
            userRecord.setTags(photoAlbumDao.tags(photoAlbum.getId()));
        } else if (userRecord.getOrderId()!=null &&userRecord.getOrderId().intValue()>0) {
            OrderDetail orderDetail = orderMapper.loadOrderDetail(userRecord.getOrderId());
            if (orderDetail!=null&&orderDetail.getPhotoAlbumID() != null && orderDetail.getPhotoAlbumID().intValue() > 0 && StringUtils.isEmpty(orderDetail.getPhotoAlbumModelCover())) {
                Photo photo = photoAlbumDao.photoinfo(orderDetail.getPhotoAlbumID(),0);
                if(photo!=null)
                    orderDetail.setPhotoAlbumModelCover(photo.getFullPath());
                userRecord.setTags(photoAlbumDao.tags(orderDetail.getPhotoAlbumID()));
            }
            userRecord.setOrderDetail(orderDetail);
        }
    }

    @Override
    public UserRecord loadUserRecord(String id) {
        return saleMapper.loadUserRecord(id);
    }

    /**
     * 根据订单ID获取当前用户的交易信息
     * 不包含青豆交易的记录
     */
    public UserRecord loadUserRecordByOrderId(UserRecord record){
        return saleMapper.loadUserRecordByOrderId(record);
    }

    @Override
    public List<ApplyCashout> queryApplyCashout(ApplyCashout applyCashout) {
        return saleMapper.queryApplyCashout(applyCashout);
    }

    @Override
    public int insertUserAccount(UserAccount userAccount) {
        return saleMapper.insertUserAccount(userAccount);
    }

    @Override
    public int updateUserAccount(UserAccount userAccount) {

        return saleMapper.updateUserAccount(userAccount);
    }

    public Map<String, Object> NowBalanceCashout(UserAccount userAccount){
        Map<String, Object> map = new HashMap<>();
        ApplyCashout applyCashout_ = new ApplyCashout();
        applyCashout_.setUserId(userAccount.getUserId());
        Byte[] bytes = {0, 1, 2, 4};
        applyCashout_.setStatuss(bytes);//未支付成功，即有冻结
        List<ApplyCashout> applyCashout = saleMapper.queryApplyCashout(applyCashout_);
        Double nowBalance =  userAccount.getBalance().doubleValue();
        BigDecimal totalCashout = new BigDecimal(0);
        if (applyCashout.size() != 0) {
            for (ApplyCashout a : applyCashout) {
                totalCashout = totalCashout.add(a.getCashout());//总冻结金额
            }
            nowBalance = userAccount.getBalance().subtract(totalCashout).doubleValue();
        }
        map.put("nowBalance", nowBalance);//当前可用余额，不含冻结金额
        map.put("totalCashout", totalCashout);//总冻结金额
        map.put("applyCashout", applyCashout);//未完成的提现状态
        return map;
    }
}
