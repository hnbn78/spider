package com.a3.lottery.domain;

public class XycIssueAM {

    private Integer code;
    private boolean status;
    private XycIssueInfo data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public XycIssueInfo getData() {
        return data;
    }

    public void setData(XycIssueInfo data) {
        this.data = data;
    }

}
