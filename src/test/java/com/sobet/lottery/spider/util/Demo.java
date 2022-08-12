package com.sobet.lottery.spider.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.a3.lottery.domain.LotteryIssueResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Demo {

	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public static void main(String[] args) {
		File file = new File("C:\\\\Users\\\\Administrator\\\\Desktop\\\\2019\\\\hash");
		File[] listFiles = file.listFiles();
		StringBuilder str = new StringBuilder();
		
		for(File tempFile : listFiles) {
			String name = tempFile.getName();
			int indexOf = name.indexOf(".");
			String realName = name.substring(0,indexOf);
			System.out.println(realName);
			
			String tjStr = tj2(realName);
			str.append(tjStr);
		}
		try {
			File file2 = new File("C:\\\\Users\\\\Administrator\\\\Desktop\\\\2019\\\\tj2.txt");
			FileUtils.write(file2, str.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static  String tj2(String dateStr) {
		try {
			int zArr[][]= new int[5][10];
			
			String path1 = "C:\\Users\\Administrator\\Desktop\\2019\\hash\\"+dateStr+".json";
			String json1 = FileUtils.readFileToString(new File(path1), "utf-8");
			List<LotteryIssueResult> list1 = gson.fromJson(json1, new TypeToken<List<LotteryIssueResult>>(){}.getType());
			  
			  int cout = list1.size();
			  
			  for(LotteryIssueResult issue : list1) {
				  String code = issue.getCode();
				  
				  String[] arr = code.split("\\|");
				  List<Integer> last5NumberList = getLast5NumberList(arr[2]);
				  for(int i  = 0; i<last5NumberList.size(); i++) {
					  int num = last5NumberList.get(i);
					  zArr[i][num]++;
				  }
			  }
			  StringBuilder str = new StringBuilder();
			  str.append("日期:").append(dateStr).append("统计如下:\n");
			  
			  for(int i = 0;i < 5;i++) {
				  String weiMark = getWeiMark(i);
				  
				  for(int j = 0; j< 10; j++) {
					  int tempCount = zArr[i][j];
					  BigDecimal pe = new BigDecimal(tempCount*100).divide(new BigDecimal(cout), 4,BigDecimal.ROUND_DOWN);
					  str.append(weiMark).append(":").append(j).append("出现的次数:").append(tempCount).append("出现的概率:").append(pe.doubleValue()).append("%").append("\n");
				  }
				  str.append("---------------------------------------------------").append("\n");
			  }
			  str.append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n");
			  System.out.println(str.toString());
			  return str.toString();
			  
		}  catch (Exception e) {
			e.printStackTrace();
		}
	 	return "";
	}
	
	private static String getWeiMark(int i) {
		String remark = "";
		switch (i) {
		case 0:
			remark="万位";
			break;
		case 1:
			remark="千位";
			break;
		case 2:
			remark="百位";
			break;
		case 3:
			remark="十位";
			break;
		case 4:
			remark="个位";
			break;

		default:
			break;
		}
		return remark;
	}

	public static  String tj(String dateStr) {
		 try {
				int zhuangArr[] = new int[10];
				int qiansanxianArr[] = new int[10];
				int zhongsanxianArr[] = new int[10];
				int housanxianArr[] = new int[10];
				
				String path1 = "C:\\Users\\Administrator\\Desktop\\2019\\hash\\"+dateStr+".json";
				String json1 = FileUtils.readFileToString(new File(path1), "utf-8");
				List<LotteryIssueResult> list1 = gson.fromJson(json1, new TypeToken<List<LotteryIssueResult>>(){}.getType());
				  
				  int cout = list1.size();
				  
				  for(LotteryIssueResult issue : list1) {
					  String code = issue.getCode();
					  
					  String[] arr = code.split("\\|");
					  List<Integer> last5NumberList = getLast5NumberList(arr[2]);
					  int zhuangSum  = getSum(last5NumberList,0,4);
					  int zhuangMod = zhuangSum%10;
					  
					  zhuangArr[zhuangMod]++;
					  
					  int qiansanxianSum  = getSum(last5NumberList,0,2);
					  int zhongsanxianSum  = getSum(last5NumberList,1,3);
					  int housanxianSum  = getSum(last5NumberList,2,4);
					  
					  int qiansanxianMod = qiansanxianSum%10;
					  int zhongsanxianMod = zhongsanxianSum%10;
					  int housanxianMod = housanxianSum%10;
					  
					  qiansanxianArr[qiansanxianMod]++;
					  zhongsanxianArr[zhongsanxianMod]++;
					  housanxianArr[housanxianMod]++;
				  }
				  StringBuilder str = new StringBuilder();
				  str.append("日期:").append(dateStr).append("统计如下:\n");
				
				  for(int i = 0; i<zhuangArr.length;i++) {
					  int tempCount = zhuangArr[i];
					  BigDecimal pe = new BigDecimal(tempCount*100).divide(new BigDecimal(cout), 4,BigDecimal.ROUND_DOWN);
					  str.append("庄牛").append(i).append("出现次数:").append(tempCount).append("出现的概率:").append(pe.doubleValue()).append("%").append("\n");
				  }
				  str.append("---------------------------------------------------").append("\n");
				  for(int i = 0; i<qiansanxianArr.length;i++) {
					  int tempCount = qiansanxianArr[i];
					  BigDecimal pe = new BigDecimal(tempCount*100).divide(new BigDecimal(cout), 4,BigDecimal.ROUND_DOWN);
					  str.append("前三牛").append(i).append("出现次数:").append(tempCount).append("出现的概率:").append(pe.doubleValue()).append("%").append("\n");
				  }
				  str.append("---------------------------------------------------").append("\n");
				  for(int i = 0; i<zhongsanxianArr.length;i++) {
					  int tempCount = zhongsanxianArr[i];
					  BigDecimal pe = new BigDecimal(tempCount*100).divide(new BigDecimal(cout), 4,BigDecimal.ROUND_DOWN);
					  str.append("中三牛").append(i).append("出现次数:").append(tempCount).append("出现的概率:").append(pe.doubleValue()).append("%").append("\n");
				  }
				  str.append("---------------------------------------------------").append("\n");
				  for(int i = 0; i<housanxianArr.length;i++) {
					  int tempCount = housanxianArr[i];
					  BigDecimal pe = new BigDecimal(tempCount*100).divide(new BigDecimal(cout), 4,BigDecimal.ROUND_DOWN);
					  str.append("后三牛").append(i).append("出现次数:").append(tempCount).append("出现的概率:").append(pe.doubleValue()).append("%").append("\n");
				  }
				  str.append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n").append("\n");
				  System.out.println(str.toString());
				  return str.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 	return "";
	}
	
	
	private static int getSum(List<Integer> last5NumberList, int startIndex, int endIndex) {
		int sum = 0;
		for(int i = startIndex;i<=endIndex;i++) {
			sum+=last5NumberList.get(i);
		}
		return sum;
	}


	public static List<Integer> getLast5NumberList(String txid) {
		List<Integer> list = new ArrayList<>();
		String[] arr = txid.split("");
		
		for(int i=arr.length-1;i>=0;i--) {
			if(StringUtils.isNumeric(arr[i])) {
				list.add(Integer.parseInt(arr[i]));
				if(list.size()>=5) {
					Collections.reverse(list);
					return list;
				}
			}
		}
		return list;
	}
}
