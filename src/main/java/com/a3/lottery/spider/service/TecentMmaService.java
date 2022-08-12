package com.a3.lottery.spider.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

public class TecentMmaService {

    private static ConcurrentSkipListMap<String, ConcurrentLinkedQueue<Long>> ISUESS_CODE_MAP = new ConcurrentSkipListMap<>();

    public static ConcurrentLinkedQueue<Long> getQueueByIssueNo(String issueNo) {
        ConcurrentLinkedQueue<Long> queue = null;

        synchronized (ISUESS_CODE_MAP) {
            queue = ISUESS_CODE_MAP.get(issueNo);
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<Long>();
                ISUESS_CODE_MAP.put(issueNo, queue);
            }
        }
        return queue;
    }

    public static void putCodeToQueue(Long openCode, String issue) {
        ConcurrentLinkedQueue<Long> queue = getQueueByIssueNo(issue);
        queue.add(openCode);

    }

    public static void clearByIssue(String issue) {
        ISUESS_CODE_MAP.remove(issue);
    }

    public static void main1(String[] args) {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -1);
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
        System.out.println(issue);
    }

    public static String buidlTencentIssue() {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        Calendar c = Calendar.getInstance();
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
    
    public static String buidl1440IssueByTimeStamp(Long timeStamp) {
    	
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(timeStamp));
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

    public static String buidlTencent30SSCIssue() {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        Calendar c = Calendar.getInstance();
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
        index = index + 1;
        String issue = dateStr + "-" + String.format("%04d", index);
        return issue;
    }

    public static String buidlLastTencentIssue() {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -1);
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

    public static Long buildLastIssueCode(String lastIssue, ConcurrentLinkedQueue<Long> lastIssueQueue) {
        synchronized (lastIssueQueue) {
            Map<Long, Integer> countMap = new HashMap<>();
            while (!lastIssueQueue.isEmpty()) {
                Long tempCode = lastIssueQueue.poll();
                Integer count = countMap.get(tempCode);

                if (count == null) {
                    count = 1;
                } else {
                    count = count + 1;
                }
                countMap.put(tempCode, count);
            }
            Map<Long, Integer> sortMap = sortMap(countMap);
            List<Long> list = new ArrayList<>(sortMap.keySet());
            clearByIssue(lastIssue);
            return list.get(0);
        }
    }

    public static Map sortMap(Map oldMap) {
        ArrayList<Map.Entry<Long, Integer>> list = new ArrayList<Map.Entry<Long, Integer>>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Long, Integer>>() {

            @Override
            public int compare(Entry<Long, Integer> arg0, Entry<Long, Integer> arg1) {
                return arg1.getValue() - arg0.getValue();
            }
        });
        Map newMap = new LinkedHashMap();
        for (int i = 0; i < list.size(); i++) {
            newMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return newMap;
    }

    public static void main(String[] args) {
        Map<Long, Integer> sortMap = new HashMap<>();
        sortMap.put(22222L, 5);
        sortMap.put(333L, 50);
        sortMap.put(444L, 4);
        sortMap.put(66L, 100);
        Map sortMap2 = sortMap(sortMap);
        List<Long> list = new ArrayList<>(sortMap.keySet());

        System.out.println(list.get(0));
    }
}
