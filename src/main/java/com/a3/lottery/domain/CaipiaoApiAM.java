package com.a3.lottery.domain;

public class CaipiaoApiAM {

    private String errorCode;
    private String message;
    private CaipiaoApiData result;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CaipiaoApiData getResult() {
        return result;
    }

    public void setResult(CaipiaoApiData result) {
        this.result = result;
    }

}
