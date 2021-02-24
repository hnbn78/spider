package com.sobet.lottery.spider.util;

import java.util.ArrayList;
import java.util.List;

public class Separate {

	public List<String> separate(String content) {
		String[] separated = content.split(":");

		String[][] item = new String[separated.length][];

		int i = 0;

		for (String section : separated) {
			item[i++] = section.split(",");
		}

		List<String> contentList = new ArrayList<String>();

		for (i = 0; i < item.length; i++) {
			if (i == 0) {
				for (String it : item[i]) {
					contentList.add(it);
				}
			} else {
				List<String> list = new ArrayList<String>();

				for (String a : contentList.toArray(new String[0])) {
					for (String b : item[i]) {
						list.add(a + ',' + b);
					}
				}

				contentList = list;
			}
		}
		return contentList;
	}

	public static void main(String[] args) {
		long starttime = System.currentTimeMillis();
		List<String> list = new Separate()
				.separate("0,1,2,3,4,5,6,7,8,9:0,1,2,3,4,5,6,7,8,9:0,1,2,3,4,5,6,7,8,9:0,1,2,3,4,5,6,7,8,9:0,1,2,3,4,5,6,7,8,9");
		
	}

}
