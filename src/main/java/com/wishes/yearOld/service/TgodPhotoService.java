package com.wishes.yearOld.service;

import java.util.List;

import com.wishes.yearOld.model.TgodPhoto;

/**
 * Created by tmg-yesky on 2016/10/11.
 */
public interface TgodPhotoService {
    int deleteByPrimaryKey(Integer id);

    int insert(TgodPhoto record);

    int insertSelective(TgodPhoto record);

    TgodPhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TgodPhoto record);

    int updateByPrimaryKey(TgodPhoto record);

    List<TgodPhoto> queryTgodPhotos(TgodPhoto record);

    int deleteAlbumPhotos(Integer albumId);
}
