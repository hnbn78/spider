package com.a3.lottery.spider.decode;

public interface IssueConverter {

	public String convert(String expect);

	public String revert(String issueNo);

}
