package com.wishes.yearOld.service;

import com.wishes.yearOld.model.RecordQingdou;
import com.wishes.yearOld.model.SalesRecord;

import java.util.List;

/**
 * Created by tmg-yesky on 2016/10/22.
 */
public interface SalesRecordService {

    int saveSalesRecord(SalesRecord record);

    SalesRecord findSalesRecordByOrderId(SalesRecord record);

    public List<RecordQingdou> queryRecordQingdouS(int userId, int pageNo,int pageSize);
}
