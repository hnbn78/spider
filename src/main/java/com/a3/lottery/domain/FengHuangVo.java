package com.a3.lottery.domain;

import java.util.List;

public class FengHuangVo {
	private Boolean status;
	private String msg;
	private List<FengHuangItemVo> data;
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<FengHuangItemVo> getData() {
		return data;
	}
	public void setData(List<FengHuangItemVo> data) {
		this.data = data;
	}
	
	
	
}
