package com.a3.lottery.domain;

import java.util.List;

public class HuoBiAM {
	private String ch;
	private String status;
	private String ts;
	private List<HuoBiItem> data;
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public List<HuoBiItem> getData() {
		return data;
	}
	public void setData(List<HuoBiItem> data) {
		this.data = data;
	}
	
	
}
