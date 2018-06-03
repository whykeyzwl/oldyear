package com.tmg.tgod;

import com.alibaba.fastjson.JSON;
import com.wishes.yearOld.dao.IPersonMapper;
import com.wishes.yearOld.service.IPersonService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-19
 * Time: 下午5:53
 * To change this template use File | Settings | File Templates.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})

public class PersonTest {

    @Autowired
    IPersonService personService;



    @Before
    public void before(){
    }

    @After
    public void After(){
    }

    @Test
    public void get() {
        System.out.println("1212");
    }

    @Test
    public void add(){
    }

}

