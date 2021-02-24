package com.a3.lottery.domain;

public class CaipiaoApiItem {

    private String gid;
    private String award;
    private String nextOpenIssue;
    private String time;
    private String nextOpenTime;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getNextOpenIssue() {
        return nextOpenIssue;
    }

    public void setNextOpenIssue(String nextOpenIssue) {
        this.nextOpenIssue = nextOpenIssue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNextOpenTime() {
        return nextOpenTime;
    }

    public void setNextOpenTime(String nextOpenTime) {
        this.nextOpenTime = nextOpenTime;
    }

}
