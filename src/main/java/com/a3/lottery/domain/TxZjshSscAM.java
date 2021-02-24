package com.a3.lottery.domain;

public class TxZjshSscAM {

    private Long index;
    private String time;
    private String code;
    private String issue;
    private String yuLiu;
    private String pk10Code;
    private String hashValue;

    public String getPk10Code() {
        return pk10Code;
    }

    public void setPk10Code(String pk10Code) {
        this.pk10Code = pk10Code;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getYuLiu() {
        return yuLiu;
    }

    public void setYuLiu(String yuLiu) {
        this.yuLiu = yuLiu;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

}
