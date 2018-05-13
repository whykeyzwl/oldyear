package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.AppVersion;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
@Component("appVersionDao")
public interface AppVersionDao {

    public AppVersion getAppVersion(@Param("type") Integer type);
}
