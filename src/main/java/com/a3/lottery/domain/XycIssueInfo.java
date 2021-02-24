package com.a3.lottery.domain;

import java.util.List;

public class XycIssueInfo {

    private String count;
    private List<XycIssueItem> info;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<XycIssueItem> getInfo() {
        return info;
    }

    public void setInfo(List<XycIssueItem> info) {
        this.info = info;
    }

}
