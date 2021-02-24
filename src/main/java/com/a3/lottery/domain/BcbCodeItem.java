package com.a3.lottery.domain;

import java.util.Date;

public class BcbCodeItem {

    private String number;
    private String data;
    private String time;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getTime());
    }
}
