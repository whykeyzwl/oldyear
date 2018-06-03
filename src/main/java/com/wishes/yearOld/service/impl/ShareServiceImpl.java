package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.dao.IUserMapper;
import com.wishes.yearOld.dao.RecordQingdouMapper;
import com.wishes.yearOld.model.PhotoAlbum;
import com.wishes.yearOld.model.RecordQingdou;
import com.wishes.yearOld.model.User;
import com.wishes.yearOld.service.ShareService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2016/12/22
 * Description:
 */

@Service("shareService")
public class ShareServiceImpl implements ShareService {

    @Autowired
    IUserMapper userMapper;

    @Autowired
    RecordQingdouMapper recordQingdouMapper;

    @Override
    public int updateQDPayForInvite(User user, Integer qingdou) {
        if (user != null && qingdou != null && qingdou > 0) {
            RecordQingdou record = new RecordQingdou();
            record.setUserId(user.getId());
            record.setRecType(Constant.QD_INCOME_INVITE);
            record.setQingdouAmount(qingdou);
            recordQingdouMapper.insert(record);
            userMapper.updateValue("qingdou", user.getQingdou() + qingdou, user.getId());
            return 1;
        }
        return 0;
    }

    @Override
    public int getTodayInviteTimes(User user) {
        if (user != null) {
            RecordQingdou record = new RecordQingdou();
            record.setUserId(user.getId());
            record.setRecType(Constant.QD_INCOME_INVITE);
            record.setCreateTime(new Date());
            return recordQingdouMapper.getTodayInviteTimes(record);
        }
        return -1;
    }

    @Override
    public Boolean ifEverShare(User user, Integer albumId) {
        if (user != null) {
            RecordQingdou record = new RecordQingdou();
            record.setUserId(user.getId());
            record.setAlbumId(albumId);
            record.setRecType(Constant.QD_INCOME_SHARE);
            record.setCreateTime(new Date());
            RecordQingdou record_ = recordQingdouMapper.queryRecordQingdou(record);
            if (record_ != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int insert(RecordQingdou record) {
        return recordQingdouMapper.insert(record);
    }

    @Override
    public RecordQingdou queryRecordQingdou(RecordQingdou record) {
        return recordQingdouMapper.queryRecordQingdou(record);
    }

    @Override
    public Boolean isHourLater(Date date, int hour) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a = format.format(cal.getTime());
        long time1 = 0;
        long time2 = 0;
        cal.setTime(format.parse(a));
        time1 = cal.getTimeInMillis();
        cal.setTime(new Date());
        time2 = cal.getTimeInMillis();
        long between_hours = time2 - time1;
        if (between_hours > 0)
            return true;

        return false;
    }

    @Override
    public int queryRecordCount(RecordQingdou record) {
        return recordQingdouMapper.queryRecordCount(record);
    }
}
