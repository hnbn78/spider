package com.a3.lottery.domain.config;

public class PlatformConfig extends WorkerConfig{
	private String name;
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "PlatformConfig [name=" + name + ", url=" + url + "]";
	}

	@Override
	public String getWorkerKey() {
		return name;
	}

}
