package com.wishes.yearOld.service.impl;


import org.springframework.stereotype.Service;

import com.wishes.yearOld.dao.TgodPhotoMapper;
import com.wishes.yearOld.model.TgodPhoto;
import com.wishes.yearOld.service.TgodPhotoService;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by tmg-yesky on 2016/10/11.
 */
@Service("photoService")
public class TgodPhotoServiceImpl implements TgodPhotoService {

    @Resource
    private TgodPhotoMapper photo;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return photo.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(TgodPhoto record) {
        return photo.insert(record);
    }

    @Override
    public int insertSelective(TgodPhoto record) {
        return photo.insertSelective(record);
    }

    @Override
    public TgodPhoto selectByPrimaryKey(Integer id) {
        return photo.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(TgodPhoto record) {
        return photo.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TgodPhoto record) {
        return photo.updateByPrimaryKey(record);
    }

    @Override
    public List<TgodPhoto> queryTgodPhotos(TgodPhoto record) {
        return photo.queryTgodPhotos(record);
    }

    @Override
    public int deleteAlbumPhotos(Integer albumId) {
        return photo.deleteAlbumPhotos(albumId);
    }
}
