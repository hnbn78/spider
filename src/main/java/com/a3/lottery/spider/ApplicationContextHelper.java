package com.a3.lottery.spider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.a3.lottery.spider.core.BossGroup;

/**
 *
 */
@WebListener()
public class ApplicationContextHelper implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContextHelper.class);

	public static ApplicationContext instance;

	private BossGroup bossGroup;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		instance = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		try {
			bossGroup = instance.getBean(BossGroup.class);
			bossGroup.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		bossGroup.shutdown();

	}

}
