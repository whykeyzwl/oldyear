package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.Person;

import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-19
 * Time: 下午1:32
 * To change this template use File | Settings | File Templates.
 */

@Component("personDao")
public interface IPersonMapper {

    Person getPerson(Person person);
}
