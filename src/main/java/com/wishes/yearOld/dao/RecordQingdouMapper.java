package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.RecordQingdou;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("recordQingdouMapper")
public interface RecordQingdouMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecordQingdou record);

    int insertSelective(RecordQingdou record);

    RecordQingdou selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecordQingdou record);

    int updateByPrimaryKey(RecordQingdou record);

    Integer checkAlbumPermission(RecordQingdou record);

    Integer getTodayInviteTimes(RecordQingdou record);

    RecordQingdou queryRecordQingdou(RecordQingdou record);

    Integer queryRecordCount(RecordQingdou record);

    public List<RecordQingdou> queryRecordQingdouS(@Param("userId") int userId, @Param("pageNo") int pageNo,
                                                   @Param("pageSize") int pageSize);
}