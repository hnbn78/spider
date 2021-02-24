package com.a3.lottery.spider.bean;

import java.sql.Time;

public class LotteryIssueAbstractModel {

    private int id;
    private int lotteryId;
    private Time startSellTime;
    private int sellLastTime;
    private Time lastSellTime;
    private Time firstTime;
    private int predictedOffsetTime;
    private int tradeLastTime;
    private int cancelTime;
    private int nextDay;
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

    public Time getStartSellTime() {
        return startSellTime;
    }

    public void setStartSellTime(Time startSellTime) {
        this.startSellTime = startSellTime;
    }

    public int getSellLastTime() {
        return sellLastTime;
    }

    public void setSellLastTime(int sellLastTime) {
        this.sellLastTime = sellLastTime;
    }

    public Time getLastSellTime() {
        return lastSellTime;
    }

    public void setLastSellTime(Time lastSellTime) {
        this.lastSellTime = lastSellTime;
    }

    public Time getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Time firstTime) {
        this.firstTime = firstTime;
    }

    public int getPredictedOffsetTime() {
        return predictedOffsetTime;
    }

    public void setPredictedOffsetTime(int predictedOffsetTime) {
        this.predictedOffsetTime = predictedOffsetTime;
    }

    public int getTradeLastTime() {
        return tradeLastTime;
    }

    public void setTradeLastTime(int tradeLastTime) {
        this.tradeLastTime = tradeLastTime;
    }

    public int getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(int cancelTime) {
        this.cancelTime = cancelTime;
    }

    public int getNextDay() {
        return nextDay;
    }

    public void setNextDay(int nextDay) {
        this.nextDay = nextDay;
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
        return "LotteryIssueAbstractModel [id=" + id + ", lotteryId=" + lotteryId + ", startSellTime=" + startSellTime
                + ", sellLastTime=" + sellLastTime + ", lastSellTime=" + lastSellTime + ", firstTime=" + firstTime
                + ", predictedOffsetTime=" + predictedOffsetTime + ", tradeLastTime=" + tradeLastTime + ", cancelTime="
                + cancelTime + ", desc=" + desc + ", format=" + format + "]";
    }

}
