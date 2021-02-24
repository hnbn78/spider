package com.a3.lottery.domain.config;

import java.util.List;

public class DomainConfigBean {

	private List<PlatformConfig> pushto;

	public List<PlatformConfig> getPushto() {
		return pushto;
	}

	public void setPushto(List<PlatformConfig> pushto) {
		this.pushto = pushto;
	}

	@Override
	public String toString() {
		return "DomainConfigBean [pushto=" + pushto + "]";
	}

}
