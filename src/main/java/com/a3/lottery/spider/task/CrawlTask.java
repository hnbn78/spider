package com.a3.lottery.spider.task;

public class CrawlTask {
	
	public String name;
	public String UUID;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public CrawlTask setRouter() {

		return this;
	}
	
	public CrawlTask setProxy() {

		return this;
	}
	
	public CrawlTask setReponseHandler() {

		return this;
	}
	
	public CrawlTask setTimeoutHandler() {

		return this;
	}
	
	public CrawlTask setRetryHandler() {

		return this;
	}
}
