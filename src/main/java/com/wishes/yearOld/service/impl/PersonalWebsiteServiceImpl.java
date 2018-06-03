package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.dao.PersonalWebsiteDao;
import com.wishes.yearOld.model.PersonalWebsite;
import com.wishes.yearOld.service.PersonalWebsiteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
@Service("PersonalWebsiteService")
public class PersonalWebsiteServiceImpl implements PersonalWebsiteService {

    @Autowired
    private PersonalWebsiteDao personalWebsiteDao;

    @Override
    public PersonalWebsite getPersonalWebsite(PersonalWebsite personalWebsite) {
        return personalWebsiteDao.getPersonalWebsite(personalWebsite);
    }

	
}
