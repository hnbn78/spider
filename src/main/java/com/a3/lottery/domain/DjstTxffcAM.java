package com.a3.lottery.domain;

import java.util.List;

public class DjstTxffcAM {

    private String rows;
    private String code;
    private String remain;
    private List<DjstTxffcItem> data;

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public List<DjstTxffcItem> getData() {
        return data;
    }

    public void setData(List<DjstTxffcItem> data) {
        this.data = data;
    }

}
