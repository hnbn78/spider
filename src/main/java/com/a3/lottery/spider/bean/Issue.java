package com.a3.lottery.spider.bean;

import java.util.Date;

public class Issue {

    private int lotteryId;
    private Date date;
    private int lotteryIndex;
    private int index;
    private String issueNo;
    private int state;
    private int errorState;
    private Date sellStart;
    private Date sellEnd;
    private long sellTime;
    private long limitTime;
    private Date tradeEnd;
    private Date predictedTime;
    private Date openTime;
    private Date cancelTime;
    private String desc;

    public int getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(int lotteryId) {
        this.lotteryId = lotteryId;
    }

    public int getLotteryIndex() {
        return lotteryIndex;
    }

    public void setLotteryIndex(int lotteryIndex) {
        this.lotteryIndex = lotteryIndex;
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

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
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

    public Date getSellStart() {
        return sellStart;
    }

    public void setSellStart(Date sellStart) {
        this.sellStart = sellStart;
    }

    public Date getSellEnd() {
        return sellEnd;
    }

    public void setSellEnd(Date sellEnd) {
        this.sellEnd = sellEnd;
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

    public Date getTradeEnd() {
        return tradeEnd;
    }

    public void setTradeEnd(Date tradeEnd) {
        this.tradeEnd = tradeEnd;
    }

    public Date getPredictedTime() {
        return predictedTime;
    }

    public void setPredictedTime(Date predictedTime) {
        this.predictedTime = predictedTime;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
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
        return "Issue [lotteryId=" + lotteryId + ", date=" + date + ", lotteryIndex=" + lotteryIndex + ", index="
                + index + ", issueNo=" + issueNo + ", state=" + state + ", errorState=" + errorState + ", sellStart="
                + sellStart + ", sellEnd=" + sellEnd + ", sellTime=" + sellTime + ", limitTime=" + limitTime
                + ", tradeEnd=" + tradeEnd + ", predictedTime=" + predictedTime + ", openTime=" + openTime
                + ", cancelTime=" + cancelTime + ", desc=" + desc + "]";
    }

}
