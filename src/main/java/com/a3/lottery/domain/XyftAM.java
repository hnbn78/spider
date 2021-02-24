package com.a3.lottery.domain;

public class XyftAM {

    private String time;
    private XyftIssue current;
    private XyftIssue next;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public XyftIssue getCurrent() {
        return current;
    }

    public void setCurrent(XyftIssue current) {
        this.current = current;
    }

    public XyftIssue getNext() {
        return next;
    }

    public void setNext(XyftIssue next) {
        this.next = next;
    }

}
