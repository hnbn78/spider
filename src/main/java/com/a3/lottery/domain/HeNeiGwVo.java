package com.a3.lottery.domain;

import java.util.List;

public class HeNeiGwVo {
	private String next_num;
	private List<HeNeiGwItem>history;
	public String getNext_num() {
		return next_num;
	}
	public void setNext_num(String next_num) {
		this.next_num = next_num;
	}
	public List<HeNeiGwItem> getHistory() {
		return history;
	}
	public void setHistory(List<HeNeiGwItem> history) {
		this.history = history;
	}
	
	
	
}
