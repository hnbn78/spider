package com.a3.lottery.domain;

import com.google.gson.Gson;

public class ZjXianShangAM {

    private String identify;// 根据请求的参数原样返回

    private String retCode;// 返回请求处理状态，不代表业务处理状态，0代表处理成功，-1代表参数缺失或者格式不正确，-2代表解密解密失败，-3代表签名错误，其他详见附录1

    private String retData;// Json

    private String errorMessage;

    public ZjXianShangAM(String identify, String retCode, String retData, String errorMessage) {
        this.identify = identify;
        this.retCode = retCode;
        this.retData = retData;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetData() {
        return retData;
    }

    public void setRetData(String retData) {
        this.retData = retData;
    }

}
