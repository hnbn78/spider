package com.a3.lottery.domain;

import java.util.List;

public class ApiPlusBean {
	private String rows;
	private String code;
	private String remain;

	private List<ApiPlushDataBean> data;

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemain() {
		return remain;
	}

	public void setRemain(String remain) {
		this.remain = remain;
	}

	public List<ApiPlushDataBean> getData() {
		return data;
	}

	public void setData(List<ApiPlushDataBean> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ApiPlusBean [rows=" + rows + ", code=" + code + ", remain=" + remain + ", data=" + data + "]";
	}

}
