package com.sobet.lottery.spider.util;

import org.springframework.util.Base64Utils;

public class Base64Convert {

	public static String encode(String string) {
		String rst = string;
		try {
			rst = Base64Utils.encodeToString(string.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rst;
	}
}
