package com.wishes.yearOld.dao;



import java.util.List;

import org.springframework.stereotype.Component;

import com.wishes.yearOld.model.TgodPhoto;
@Component("TgodPhotoMapper")
public interface TgodPhotoMapper{
    int deleteByPrimaryKey(Integer id);

    int insert(TgodPhoto record);

    int insertSelective(TgodPhoto record);

    TgodPhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TgodPhoto record);

    int updateByPrimaryKey(TgodPhoto record);

    List<TgodPhoto> queryTgodPhotos(TgodPhoto record);

    int deleteAlbumPhotos(Integer albumId);
}