package com.a3.lottery.domain;

public class DrawResult {

    private String lottery;
    private String issue;
    private String code;
    private String time;
    private String timestamp;
    private String yuliu;// 预留字段

    public DrawResult() {
        super();
    }

    public DrawResult(String lottery, String issue, String code, String time, String timestamp, String yuliu) {
        super();
        this.lottery = lottery;
        this.issue = issue;
        this.code = code;
        this.time = time;
        this.timestamp = timestamp;
        this.yuliu = yuliu;
    }

    public String getYuliu() {
        return yuliu;
    }

    public void setYuliu(String yuliu) {
        this.yuliu = yuliu;
    }

    public String getLottery() {
        return lottery;
    }

    public void setLottery(String lottery) {
        this.lottery = lottery;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DrawResult [lottery=" + lottery + ", issue=" + issue + ", code=" + code + ", time=" + time + "]";
    }

}
