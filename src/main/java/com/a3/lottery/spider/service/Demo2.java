package com.a3.lottery.spider.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Demo2 {
	
	 private static final SimpleDateFormat SimpleParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	 
	public static void main(String[] args) {
		  try {
			 String ss = "63e85ee1ccdaad12e2d0a9b0deda9c6fb8521d8336f8a1adfe8e01a44a415151";
			 String formartHASH60PK10 = formartHASH60PK10(ss);
			 
			 System.out.println(formartHASH60PK10);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
    private static String formartHASH60PK10(String lotteryNumber) {
    	
        String[] array = {"01","02","03","04","05","06","07","08","09","10"};
        Set<String> set = new HashSet<>(Arrays.asList(array));
    	String[] arr = lotteryNumber.split("");
    	List<String> list = new ArrayList<String>();
    	
		
		for(int i=arr.length-1;i>=0;i--) {
			if(list.size() >= 10) {
				break;
			}
			String tempNum = arr[i];
			if(StringUtils.isNumeric(tempNum)) {
				int tempNumInt = Integer.parseInt(arr[i]);
				String tempStr = "";
				if(tempNumInt == 0) {
					tempStr = "10";
				}else {
					tempStr = "0"+tempNumInt;
				}
				if(set.contains(tempStr)) {
					list.add(tempStr);
					set.remove(tempStr);
				}
			}
		}
		list.addAll(new ArrayList<>(set));
		return StringUtils.join(list,",");
	}
	
	  private static String buildSteamNumber(String zaiXianNum,String zhengZaiYouXiNum) {
	    	String [] zaiXianNumArr = zaiXianNum.split("");
	    	String [] zhengZaiYouXiNumArr = zhengZaiYouXiNum.split("");
	    	List<String> list =new ArrayList<String>();
	    	
	    	for(int i=zaiXianNumArr.length-3; i <= zaiXianNumArr.length-1;i++) {
	    		list.add(zaiXianNumArr[i]);
	    	}
	    	for(int i=zhengZaiYouXiNumArr.length-2; i <= zhengZaiYouXiNumArr.length-1;i++) {
	    		list.add(zhengZaiYouXiNumArr[i]);
	    	}
	        return StringUtils.join(list,",");
	    }
	    
	
	
	 public static String buidlTencentIssue() {
	        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
	        
	        String ss = "2023-08-13 00:02:20";
	        Date date1 = null;
	        try {
				 date1 = SimpleParse.parse(ss);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        
	        

	        Calendar c = Calendar.getInstance();
	        c.setTime(date1);
	        int h = c.get(Calendar.HOUR_OF_DAY);
	        int m = c.get(Calendar.MINUTE);

	        int index = 0;
	        if (h > 0 || m > 0) {
	            index = h * 60 + m;
	        } else {
	            Integer dayTimeIndex = Integer.valueOf(dateStr);
	            dateStr = String.valueOf(dayTimeIndex - 1);
	            index = 1440;
	        }
	        String issue = dateStr + "-" + String.format("%04d", index);
	        return issue;
	    }
}
