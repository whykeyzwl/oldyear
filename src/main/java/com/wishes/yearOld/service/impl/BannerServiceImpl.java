package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.dao.BannerMapper;
import com.wishes.yearOld.dao.KeywordMapper;
import com.wishes.yearOld.model.Banner;
import com.wishes.yearOld.service.BannerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tmg-yesky on 2016/10/14.
 */
@Service("bannerService")
public class BannerServiceImpl implements BannerService {

    @Autowired
    BannerMapper banner;

    @Autowired
    KeywordMapper keywordMapper;


    @Override
    public List<Banner> queryBanners(Banner record) {
        return banner.queryBanners(record);
    }

    @Override
    public List<String> queryKeywords(){
        return keywordMapper.keywords();
    }
}
