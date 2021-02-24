package com.a3.lottery.spider.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a3.lottery.domain.config.PlatformConfig;
import com.a3.lottery.spider.ApplicationContextHelper;
import com.a3.lottery.spider.service.ApiPlusService;

public class LotteryListenTask extends Worker {
	private static Logger logger = LoggerFactory.getLogger(LotteryListenTask.class);

	private ApiPlusService apiPlusService;

	private PlatformConfig platformConfig;

	public LotteryListenTask(PlatformConfig platformConfig) {
		super();
		this.platformConfig = platformConfig;
	}

	@Override
	public void doLoop() {

		apiPlusService.listen(platformConfig);

	}

	@Override
	public void init() {
		apiPlusService = ApplicationContextHelper.instance.getBean(ApiPlusService.class);
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

}
