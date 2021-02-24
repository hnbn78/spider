package com.a3.lottery.domain;

import java.util.List;

public class BcbCodeAM {

    private String code;
    private String msg;
    private List<BcbCodeItem> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BcbCodeItem> getData() {
        return data;
    }

    public void setData(List<BcbCodeItem> data) {
        this.data = data;
    }

}
