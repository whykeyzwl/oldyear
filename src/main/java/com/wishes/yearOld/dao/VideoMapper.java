package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.Video;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("videoMapper")
public interface VideoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Video record);

    int insertSelective(Video record);

    Video selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKeyWithBLOBs(Video record);

    int updateByPrimaryKey(Video record);

    List<Video> getSyncVideo();
}