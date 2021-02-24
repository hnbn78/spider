package com.a3.lottery.domain;

public class TencentOnline {

	private String onlinetime;
	private String onlinenumber;
	private String onlinechange;

	public String getOnlinetime() {
		return onlinetime;
	}

	public void setOnlinetime(String onlinetime) {
		this.onlinetime = onlinetime;
	}

	public String getOnlinenumber() {
		return onlinenumber;
	}

	public void setOnlinenumber(String onlinenumber) {
		this.onlinenumber = onlinenumber;
	}

	public String getOnlinechange() {
		return onlinechange;
	}

	public void setOnlinechange(String onlinechange) {
		this.onlinechange = onlinechange;
	}

	@Override
	public String toString() {
		return "TencentOnline [onlinetime=" + onlinetime + ", onlinenumber=" + onlinenumber + ", onlinechange="
				+ onlinechange + "]";
	}

}
