package com.a3.lottery.spider.bean;

import java.util.Date;

public class LotteryIssueTime {

    private int id;
    private int lotteryId;
    private String issue;
    private Date date;
    private int index;
    private int startSellTime;
    private int endSellTime;
    private int endTradeTime;
    private int cancelTime;
    private int lastTime;
    private int limitTime;
    private int predictedTime;
    private int realTime;
    private String desc;
    private String format;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(int lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStartSellTime() {
        return startSellTime;
    }

    public int getEndSellTime() {
        return endSellTime;
    }

    public void setEndSellTime(int endSellTime) {
        this.endSellTime = endSellTime;
    }

    public int getEndTradeTime() {
        return endTradeTime;
    }

    public void setEndTradeTime(int endTradeTime) {
        this.endTradeTime = endTradeTime;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }

    public int getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }

    public int getPredictedTime() {
        return predictedTime;
    }

    public void setPredictedTime(int predictedTime) {
        this.predictedTime = predictedTime;
    }

    public int getRealTime() {
        return realTime;
    }

    public void setRealTime(int realTime) {
        this.realTime = realTime;
    }

    public void setStartSellTime(int startSellTime) {
        this.startSellTime = startSellTime;
    }

    public int getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(int cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "LotteryIssueTime [id=" + id + ", lotteryId=" + lotteryId + ", issue=" + issue + ", date=" + date
                + ", index=" + index + ", startSellTime=" + startSellTime + ", endSellTime=" + endSellTime
                + ", endTradeTime=" + endTradeTime + ", cancelTime=" + cancelTime + ", lastTime=" + lastTime
                + ", limitTime=" + limitTime + ", predictedTime=" + predictedTime + ", realTime=" + realTime
                + ", desc=" + desc + ", format=" + format + "]";
    }

}
