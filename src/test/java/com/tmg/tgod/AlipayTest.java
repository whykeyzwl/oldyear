package com.tmg.tgod;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.wishes.yearOld.apppay.AppPayConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-10-28
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AlipayTest {

    @Before
    public void before(){
    }

    @After
    public void After(){
    }

    @Test
    public void test() {
        AlipayClient alipayClient =
                new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                        AppPayConfig.ALIPAY_APP_ID,AppPayConfig.ALIPAY_APP_PRIVATE_KEY,"json",AppPayConfig.ALIPAY_APP_CHASET,AppPayConfig.ALIPAY_APP_ALI_PUBLIC_KEY);

    }

    @Test
    public void add(){
    }
}
