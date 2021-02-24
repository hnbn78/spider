package com.a3.lottery.spider.bean;

import java.util.Date;

public class LotteryIssue {

    public static final int STATE_NORMAL = 0;
    public static final int SEEK_STATE_NORMAL = 0;
    public static final int SEEK_STATE_RUNNING = 1;
    public static final int SEEK_STATE_FINISH = 2;
    public static final int SEEK_STATE_ERROR = 3;

    public static final int OPEN_STATE_NOT_OPEN = 0;
    public static final int OPEN_STATE_GET_CODE = 1;
    public static final int OPEN_STATE_START = 2;
    public static final int OPEN_STATE_FINISH = 3;
    public static final int OPEN_STATE_PAY = 4;
    public static final int OPEN_STATE_PAY_FAILED = 5;

    private int id;
    private int lotteryId;
    private String issueNo;

    private Date date;
    private int index;
    private int lotteryIndex;
    private int state;
    private int errorState;
    private long sellStart;
    private long sellEnd;
    private long tradeEnd;
    private long sellTime;
    private long limitTime;
    private long predictedTime;
    private int openState;
    private int openSource;
    private String openCode;
    private long openTime;
    private long cancelTime;
    private String desc;

    public volatile boolean stopSelling;

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

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLotteryIndex() {
        return lotteryIndex;
    }

    public void setLotteryIndex(int lotteryIndex) {
        this.lotteryIndex = lotteryIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getErrorState() {
        return errorState;
    }

    public void setErrorState(int errorState) {
        this.errorState = errorState;
    }

    public long getSellStart() {
        return sellStart;
    }

    public void setSellStart(long sellStart) {
        this.sellStart = sellStart;
    }

    public long getSellEnd() {
        return sellEnd;
    }

    public void setSellEnd(long sellEnd) {
        this.sellEnd = sellEnd;
    }

    public long getTradeEnd() {
        return tradeEnd;
    }

    public void setTradeEnd(long tradeEnd) {
        this.tradeEnd = tradeEnd;
    }

    public long getSellTime() {
        return sellTime;
    }

    public void setSellTime(long sellTime) {
        this.sellTime = sellTime;
    }

    public long getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(long limitTime) {
        this.limitTime = limitTime;
    }

    public long getPredictedTime() {
        return predictedTime;
    }

    public void setPredictedTime(long predictedTime) {
        this.predictedTime = predictedTime;
    }

    public int getOpenState() {
        return openState;
    }

    public void setOpenState(int openState) {
        this.openState = openState;
    }

    public int getOpenSource() {
        return openSource;
    }

    public void setOpenSource(int openSource) {
        this.openSource = openSource;
    }

    public String getOpenCode() {
        return openCode;
    }

    public void setOpenCode(String openCode) {
        this.openCode = openCode;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "LotteryIssue [lotteryId=" + lotteryId + ", issueNo=" + issueNo + ", date=" + date + ", index=" + index
                + ", state=" + state + ", errorState=" + errorState + ", sellStart=" + sellStart + ", sellEnd="
                + sellEnd + ", tradeEnd=" + tradeEnd + ", sellTime=" + sellTime + ", limitTime=" + limitTime
                + ", predictedTime=" + predictedTime + ", openState=" + openState + ", openSource=" + openSource
                + ", openCode=" + openCode + ", openTime=" + openTime + ", cancelTime=" + cancelTime + ", desc=" + desc
                + ", stopSelling=" + stopSelling + "]";
    }

}
