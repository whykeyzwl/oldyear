package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.dao.IUserMapper;
import com.wishes.yearOld.dao.PhotoAlbumDao;
import com.wishes.yearOld.dao.RecordQingdouMapper;
import com.wishes.yearOld.dao.SalesRecordMapper;
import com.wishes.yearOld.model.RecordQingdou;
import com.wishes.yearOld.model.SalesRecord;
import com.wishes.yearOld.service.SalesRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tmg-yesky on 2016/10/22.
 */
@Service("salesRecordService")
public class SalesRecordServiceImpl implements SalesRecordService {

    @Autowired
    SalesRecordMapper salesRecordMapper;
    @Autowired
    PhotoAlbumDao photoAlbumDao;
    @Autowired
    private RecordQingdouMapper recordQingdouMapper;

    @Override
    public int saveSalesRecord(SalesRecord record) {
        salesRecordMapper.insertSelective(record);
   //     photoAlbumDao.updateAlbumSales(record.getProductid());
        return 1;
    }

    @Override
    public SalesRecord findSalesRecordByOrderId(SalesRecord record) {
        return salesRecordMapper.findSalesRecordByOrderId(record);
    }

    @Override
    public List<RecordQingdou> queryRecordQingdouS(int userId, int pageNo, int pageSize) {
        return recordQingdouMapper.queryRecordQingdouS(userId,(pageNo-1)*pageSize,pageSize);
    }
}
