package com.a3.lottery.module;

import java.util.Map;

import com.a3.lottery.domain.LotteryRequest;

public interface RequestParameter {

	public Map<String, String> buildParamters(LotteryRequest lotteryRequest);
}
