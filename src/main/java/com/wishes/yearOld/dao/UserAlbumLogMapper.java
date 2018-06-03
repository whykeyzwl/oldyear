package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.UserAlbumLog;

import org.springframework.stereotype.Component;

@Component("userAlbumLogMapper")
public interface UserAlbumLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAlbumLog record);

    int insertSelective(UserAlbumLog record);

    UserAlbumLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAlbumLog record);

    int updateByPrimaryKey(UserAlbumLog record);

    UserAlbumLog queryUserAlbumLog(UserAlbumLog record);
}