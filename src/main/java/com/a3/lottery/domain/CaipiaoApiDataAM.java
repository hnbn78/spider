package com.a3.lottery.domain;

import java.util.List;

public class CaipiaoApiDataAM {
	private String businessCode;
	private String message;
	private List<CaipiaoApiItemVO> data;
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
	public List<CaipiaoApiItemVO> getData() {
		return data;
	}
	public void setData(List<CaipiaoApiItemVO> data) {
		this.data = data;
	}

	
}
