package com.a3.lottery.spider.core;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a3.lottery.domain.config.DrawConfig;
import com.a3.lottery.spider.ApplicationContextHelper;
import com.a3.lottery.spider.service.ApiPlusService;

public class LotterySpiderTask extends Worker {

    private static Logger logger = LoggerFactory.getLogger(LotterySpiderTask.class);

    private DrawConfig drawConfig;

    private ApiPlusService apiPlusService;

    private Queue<String> queue = new LinkedBlockingQueue<>();

    public LotterySpiderTask(DrawConfig drawConfig) {
        super();
        this.drawConfig = drawConfig;
        this.drawConfig.buildConvert();
    }

    @Override
    public void doLoop() {
        long start = System.currentTimeMillis();
        logger.info("Getting response,lottery:{}", drawConfig.getLotteryCode());
        apiPlusService.getResponse(drawConfig, queue);
        long end = System.currentTimeMillis();

        if ((end - start) > drawConfig.getPeriod()) {
            setSleepPeriod(0l);
        } else {
            setSleepPeriod(drawConfig.getPeriod());
        }

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
