package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.RecordDownLimit;

import org.springframework.stereotype.Component;

@Component("recordDownLimitMapper")
public interface RecordDownLimitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecordDownLimit record);

    int insertSelective(RecordDownLimit record);

    RecordDownLimit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecordDownLimit record);

    int updateByPrimaryKey(RecordDownLimit record);

    Integer checkAlbumPermission(RecordDownLimit record);

    Integer getDownLimitCount(RecordDownLimit record);

    Integer getTotalDownLimitCount(RecordDownLimit record);

    RecordDownLimit getDownLimit(RecordDownLimit record);
}