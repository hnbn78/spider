package com.a3.lottery.domain;

public class ApiPlushDataBean {
	private String expect;
	private String opencode;
	private String opentime;
	private String opentimestamp;

	public String getExpect() {
		return expect;
	}

	public void setExpect(String expect) {
		this.expect = expect;
	}

	public String getOpencode() {
		return opencode;
	}

	public void setOpencode(String opencode) {
		this.opencode = opencode;
	}

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public String getOpentimestamp() {
		return opentimestamp;
	}

	public void setOpentimestamp(String opentimestamp) {
		this.opentimestamp = opentimestamp;
	}

	@Override
	public String toString() {
		return "ApiPlushDataBean [expect=" + expect + ", opencode=" + opencode + ", opentime=" + opentime
				+ ", opentimestamp=" + opentimestamp + "]";
	}

}
