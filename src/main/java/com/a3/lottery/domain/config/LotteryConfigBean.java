package com.a3.lottery.domain.config;

import java.util.List;

public class LotteryConfigBean {
	private List<DrawConfig> apiplus;

	public List<DrawConfig> getApiplus() {
		return apiplus;
	}

	public void setApiplus(List<DrawConfig> apiplus) {
		this.apiplus = apiplus;
	}

	@Override
	public String toString() {
		return "LotteryConfigeBean [apiplus=" + apiplus + "]";
	}

}
