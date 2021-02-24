package com.sobet.lottery.spider.util;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@ActiveProfiles(Profiles.UNIT_TEST)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={ "classpath:applicationContext-spider.xml"})  
public class BaseSpringTest {
}
