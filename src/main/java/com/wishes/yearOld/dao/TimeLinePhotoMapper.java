package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.TimeLinePhoto;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("timeLinePhotoMapper")
public interface TimeLinePhotoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TimeLinePhoto record);

    int insertSelective(TimeLinePhoto record);

    TimeLinePhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TimeLinePhoto record);

    int updateByPrimaryKey(TimeLinePhoto record);

}