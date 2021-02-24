package com.a3.lottery.domain;

public class CaipiaoapiCCAM {
	private int errorCode;
	private String message;
	private CaipiaoApiDataAM result;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public CaipiaoApiDataAM getResult() {
		return result;
	}
	public void setResult(CaipiaoApiDataAM result) {
		this.result = result;
	}
	
	
}
