package com.freelycar.saas.basic;

import org.junit.After;
import org.junit.Before;

/**
 * @author tangwei - Toby
 * @date 2018/11/29
 * @email toby911115@gmail.com
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = BootApplication.class)
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
//@WebAppConfiguration
public class BaseTest {
    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

}
