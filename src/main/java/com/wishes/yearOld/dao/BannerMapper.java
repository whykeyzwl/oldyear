package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.Banner;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("banner")
public interface BannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Banner record);

    int insertSelective(Banner record);

    Banner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Banner record);

    int updateByPrimaryKey(Banner record);

    List<Banner> queryBanners(Banner record);
}