package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.SalesRecord;

import org.springframework.stereotype.Component;

@Component("salesRecordMapper")
public interface SalesRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SalesRecord record);

    int insertSelective(SalesRecord record);

    SalesRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SalesRecord record);

    int updateByPrimaryKey(SalesRecord record);

    SalesRecord findSalesRecordByOrderId(SalesRecord record);
}