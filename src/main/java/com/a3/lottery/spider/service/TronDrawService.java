package com.a3.lottery.spider.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.a3.lottery.domain.LotteryIssueResult;
import com.a3.lottery.util.HttpClientTool;
import com.google.gson.Gson;

@Service
public class TronDrawService {

    private static Logger logger = LoggerFactory.getLogger(TronDrawService.class);
    private static Gson gson = new Gson();
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    
    @Value("#{configProperties['tron.draw.count']}")
    private Integer tronDrawCount = 100;

    public LotteryIssueResult shaHao(LotteryIssueResult param,  String centerDrawUrl) {
        try {
            String issueNo = param.getIssue();
            String url = "";

            if (StringUtils.isBlank(centerDrawUrl)) {
                url = "http://3a.neibukj.com:8084/draw-agent/tron/queryIssue?issueNo=" + issueNo;
            } else {
                url = centerDrawUrl + "tron/queryIssue?issueNo=" + issueNo;
            }
            String drawStr = "0";
            try {
                drawStr = HttpClientTool.get(url);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            logger.info("奖期:{}查询中央杀号节点 是否需要杀号:{}", issueNo, drawStr);
            if (!"1".equals(drawStr)) {
                return param;
            }
            // 需要杀号
//            String eosHash = param.getBlockHash();
            Map<String, LotteryIssueResult> map = new HashMap<String, LotteryIssueResult>();
            map.put(param.getCode(), param);

            long time1 = System.currentTimeMillis();
            int threadCount = 10;
            int page = (tronDrawCount % threadCount == 0) ? tronDrawCount / threadCount
                    : (tronDrawCount / threadCount + 1);

            List<Future<Map<String, LotteryIssueResult>>> list = new ArrayList<>();
            for (int i = 0; i < threadCount; i++) {
                Future<Map<String, LotteryIssueResult>> future = executor.submit(new TronRandomCodeTask(param, page));
                list.add(future);
            }
            for (Future<Map<String, LotteryIssueResult>> future : list) {
                Map<String, LotteryIssueResult> tempMap = future.get();
                map.putAll(tempMap);
            }
            long time2 = System.currentTimeMillis();
            logger.info("生成code数量：{},花费时间:{}毫秒", map.size(), (time2 - time1));

            StringBuffer codeStr = new StringBuffer();
            codeStr.append(param.getCode());

            for (String key : map.keySet()) {
                if (codeStr.equals(param.getCode())) {
                    continue;
                }
                if (codeStr.length() > 0) {
                    codeStr.append("#");
                }
                codeStr.append(key);
            }
            Map<String, Object> queryParm = new HashMap<>();
            queryParm.put("lotteryId", 380);
            queryParm.put("issueNo", param.getIssue());
            queryParm.put("codes", codeStr.toString());

            String drawUrl = null;
            if (StringUtils.isBlank(centerDrawUrl)) {
                drawUrl = "http://3a.neibukj.com:8084/draw-agent/tron/draw";
            } else {
                drawUrl = centerDrawUrl + "tron/draw";
            }
            String drawResponse = "-1";
            logger.info("请求杀号地址：{},参数:{}", drawUrl,queryParm);
            try {
                drawResponse = HttpClientTool.post(drawUrl, queryParm);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            logger.info("杀号结果返回：{}", drawResponse);
            if ("-1".equals(drawResponse)) {
                return param;
            }
            LotteryIssueResult resultVo = map.get(StringUtils.trim(drawResponse));

            if (resultVo == null) {
                return param;
            }
            logger.info("需要杀号杀号 前开奖详情：{}", param);
            param.setBlockHash(resultVo.getBlockHash());
            param.setShaHash(resultVo.getShaHash());
            param.setCode(resultVo.getCode());
            logger.info("需要杀号杀号 后开奖详情：{}", param);
            return param;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return param;
        }
    }
}
