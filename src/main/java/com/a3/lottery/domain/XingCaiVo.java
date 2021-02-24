package com.a3.lottery.domain;

import java.util.List;

public class XingCaiVo {
	private String code;
	private String msg;
	private int rows;
	private List<XingCaiItemVo> data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public List<XingCaiItemVo> getData() {
		return data;
	}
	public void setData(List<XingCaiItemVo> data) {
		this.data = data;
	}
	
	
	
	
	
}
