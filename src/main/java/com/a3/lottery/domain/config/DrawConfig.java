package com.a3.lottery.domain.config;

import org.apache.commons.lang3.StringUtils;

import com.a3.lottery.spider.decode.IssueConverter;
import com.a3.lottery.spider.decode.SimpleIssueConverter;

public class DrawConfig extends WorkerConfig {

    private String lotteryCode;
    private String toCode;
    private String issueConverter;
    private Long period;
    private String route;
    private IssueConverter converter;
    private String filePath;
    private String type;
    private String spiderIp;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSpiderIp() {
        return spiderIp;
    }

    public void setSpiderIp(String spiderIp) {
        this.spiderIp = spiderIp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public String getToCode() {
        return toCode;
    }

    public void setToCode(String toCode) {
        this.toCode = toCode;
    }

    public String getIssueConverter() {
        return issueConverter;
    }

    public void setIssueConverter(String issueConverter) {
        this.issueConverter = issueConverter;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public IssueConverter getConverter() {
        return converter;
    }

    @Override
    public String toString() {
        return "DrawConfig [lotteryCode=" + lotteryCode + ", toCode=" + toCode + ", issueConverter=" + issueConverter
                + ", period=" + period + "]";
    }

    @Override
    public String getWorkerKey() {
        return lotteryCode;
    }

    public void buildConvert() {
        if (!StringUtils.isEmpty(issueConverter)) {
            String[] strs = issueConverter.split("\\|");
            IssueConverter converter = new SimpleIssueConverter(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]));
            this.converter = converter;
        }
    }

}
