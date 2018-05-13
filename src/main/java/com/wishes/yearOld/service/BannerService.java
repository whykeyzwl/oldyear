package com.wishes.yearOld.service;

import com.wishes.yearOld.model.Banner;

import java.util.List;

/**
 * Created by tmg-yesky on 2016/10/14.
 */
public interface BannerService {

    List<Banner> queryBanners(Banner record);

    List<String> queryKeywords();

}
