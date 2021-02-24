package com.a3.lottery.domain;

import java.util.List;

public class CaipiaoApiData {

    private String businessCode;
    private String message;
    private List<CaipiaoApiItem> data;

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CaipiaoApiItem> getData() {
        return data;
    }

    public void setData(List<CaipiaoApiItem> data) {
        this.data = data;
    }

}
