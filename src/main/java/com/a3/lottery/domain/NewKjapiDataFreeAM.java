package com.a3.lottery.domain;

import java.util.List;

public class NewKjapiDataFreeAM {
	private int businessCode;
	private String message;
	private List<NewKjapiFreeItem> data;
	public int getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(int businessCode) {
		this.businessCode = businessCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<NewKjapiFreeItem> getData() {
		return data;
	}
	public void setData(List<NewKjapiFreeItem> data) {
		this.data = data;
	}

	
}
