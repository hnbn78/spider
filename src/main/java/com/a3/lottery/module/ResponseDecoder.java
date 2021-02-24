package com.a3.lottery.module;

public interface ResponseDecoder {

	public void onFailed();
	
	public void onTimeout();
	
	public void onAuthentokenFiled();
	
	public void onSuccess(String content);
	
}
