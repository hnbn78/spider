package com.a3.lottery.spider.bean;

public class LotterySpider {

	private int id;
	private int lotteryId;
	private String lotteryCode;

	private int switchon;
	private int status;
	
	public static final int SWITCH_OFF = 0; //关闭
	public static final int SWITCH_ON = 1; //开启
	
	public static final int STATUS_DEFAULT = 0; //停止
	public static final int STATUS_ = 0; //启动中

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(int lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getLotteryCode() {
		return lotteryCode;
	}

	public void setLotteryCode(String lotteryCode) {
		this.lotteryCode = lotteryCode;
	}

	public int getSwitchon() {
		return switchon;
	}

	public void setSwitchon(int switchon) {
		this.switchon = switchon;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LotterySpider [id=" + id + ", lotteryId=" + lotteryId + ", lotteryCode=" + lotteryCode + ", status="
				+ status + "]";
	}

}
