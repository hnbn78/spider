package com.a3.lottery.spider.task;

public class CrawlerTaskBuilder {

	private CrawlTask task;

	public CrawlerTaskBuilder() {
		super();
	}

	public static CrawlerTaskBuilder create(String name) {
		CrawlerTaskBuilder builder = new CrawlerTaskBuilder();
		
		builder.task = new CrawlTask();
		builder.task.setName(name);
		
		return builder;
	}

	public CrawlerTaskBuilder setId(String UUID) {
		task.setUUID(UUID);
		return this;
	}

	public CrawlerTaskBuilder addTragetUrl(String... url) {
		
		
		return this;
	}

	public CrawlTask build() {
		return null;
	}

}
