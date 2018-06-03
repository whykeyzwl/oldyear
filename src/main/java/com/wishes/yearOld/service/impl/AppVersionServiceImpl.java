package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.dao.AppVersionDao;
import com.wishes.yearOld.model.AppVersion;
import com.wishes.yearOld.service.AppVersionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
@Service("appVersionService")
public class AppVersionServiceImpl implements AppVersionService {

    @Autowired
    private AppVersionDao appVersionDao;

    @Override
    public AppVersion getAppVersion(int type) {
        return appVersionDao.getAppVersion(type);
    }
}
