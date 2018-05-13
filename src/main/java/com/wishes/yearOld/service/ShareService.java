package com.wishes.yearOld.service;

import com.wishes.yearOld.model.RecordQingdou;
import com.wishes.yearOld.model.User;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2016/12/22
 * Description:
 */
public interface ShareService {

    int updateQDPayForInvite(User user, Integer qingdou);

    int getTodayInviteTimes(User user);

    Boolean ifEverShare(User user,Integer albumId);

    int insert(RecordQingdou record);

    RecordQingdou queryRecordQingdou(RecordQingdou record);

    Boolean isHourLater(Date date,int hour) throws ParseException;

    int queryRecordCount(RecordQingdou record);
}
