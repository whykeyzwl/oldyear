package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.dao.IPersonMapper;
import com.wishes.yearOld.model.Person;
import com.wishes.yearOld.service.IPersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-19
 * Time: 下午1:40
 * To change this template use File | Settings | File Templates.
 */
@Service("personService")
public class PersonServiceImpl implements IPersonService{


    @Resource
    private IPersonMapper personDao;


    @Override
    public Person getPerson(Person person) {
        return personDao.getPerson(person);
    }
}
