package com.wishes.yearOld.dao;
import com.wishes.yearOld.model.PersonalWebsite;

import org.springframework.stereotype.Component;
/**
 * Created by IntelliJ IDEA.
 * User: zwl
 * Date: 2017/04/26
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */
@Component("PersonalWebsiteDao")
public interface PersonalWebsiteDao {
    /**
     * 取得用户信息
     * @param personalWebsite
     * @return
     */
    public PersonalWebsite getPersonalWebsite(PersonalWebsite personalWebsite);
}
