package com.a3.lottery.domain;

public class XyftIssue {

    private String periodNumber;
    private String awardTime;
    private String periodDate;
    private String awardTimeInterval;
    private String delayTimeInterval;

    public String getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(String periodNumber) {
        this.periodNumber = periodNumber;
    }

    public String getAwardTime() {
        return awardTime;
    }

    public void setAwardTime(String awardTime) {
        this.awardTime = awardTime;
    }

    public String getPeriodDate() {
        return periodDate;
    }

    public void setPeriodDate(String periodDate) {
        this.periodDate = periodDate;
    }

    public String getAwardTimeInterval() {
        return awardTimeInterval;
    }

    public void setAwardTimeInterval(String awardTimeInterval) {
        this.awardTimeInterval = awardTimeInterval;
    }

    public String getDelayTimeInterval() {
        return delayTimeInterval;
    }

    public void setDelayTimeInterval(String delayTimeInterval) {
        this.delayTimeInterval = delayTimeInterval;
    }

}
