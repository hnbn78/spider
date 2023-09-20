package com.a3.lottery.spider.service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a3.lottery.domain.LotteryIssueResult;
import com.a3.lottery.domain.config.DrawConfig;
import com.a3.lottery.module.LotteryIssueResultComparator;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;

public class LotteryResultSaveTask extends Thread {

    private static Logger logger = LoggerFactory.getLogger(LotteryResultSaveTask.class);

    private DrawConfig drarConfig;
    private LotteryIssueResult lotteryIssueResult;
    private static Gson gson = new Gson();

    public LotteryResultSaveTask(DrawConfig drarConfig, LotteryIssueResult lotteryIssueResult) {
        super();
        this.drarConfig = drarConfig;
        this.lotteryIssueResult = lotteryIssueResult;
    }

    @Override
    public void run() {
        onResult(drarConfig, lotteryIssueResult);
    }

    public void onResult(DrawConfig drarConfig, LotteryIssueResult issueResult) {

        try {
            writeResult(drarConfig, issueResult);

            saveRecentResult(drarConfig, issueResult);

            saveIssueResult(drarConfig, issueResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "serial" })
    private void writeResult(DrawConfig drarConfig, LotteryIssueResult issueResult) {
        List<LotteryIssueResult> results = null;
        String dayString = getDayString(issueResult);
        String dayFileName = getDayFileName(drarConfig, dayString);
        try {
            String contents = FileUtils.readFileToString(new File(dayFileName));
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(StringUtils.trim(contents)));
            reader.setLenient(true);
            results = gson.fromJson(reader, new TypeToken<ArrayList<LotteryIssueResult>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (results == null) {
            results = new ArrayList();
        } else {
            Iterator<LotteryIssueResult> it = results.iterator();
            while (it.hasNext()) {
                LotteryIssueResult lotteryIssue = (LotteryIssueResult) it.next();
                if (issueResult.getIssue().equals(lotteryIssue.getIssue())) {
                    it.remove();
                }
            }
        }
        results.add(issueResult);

        results.sort(new LotteryIssueResultComparator());
        try {
            String contents = gson.toJson(results);
            logger.info("saving to file..result:{},file{}", issueResult, drarConfig.getFilePath());
            FileUtils.write(new File(dayFileName), contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveIssueResult(DrawConfig drarConfig, LotteryIssueResult issueResult) {
        String issueString = getDayString(issueResult) + "/" + issueResult.getIssue();
        String issueFileName = getDayFileName(drarConfig, issueString);
        try {
            String contents = gson.toJson(issueResult);
            logger.info("saving to file..result:{},file{}", issueResult, issueFileName);
            FileUtils.write(new File(issueFileName), contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("serial")
    private void saveRecentResult(DrawConfig drarConfig, LotteryIssueResult issueResult) {
        String recentFileName = getRecentFileName(drarConfig);
        List<LotteryIssueResult> results = null;
        File file = new File(recentFileName);
		try {
            String contents = FileUtils.readFileToString(file);
            results = gson.fromJson(StringUtils.trim(contents), new TypeToken<ArrayList<LotteryIssueResult>>() {
            }.getType());
        } catch (MalformedJsonException e) {
            e.printStackTrace();
            boolean deleteFlag = false;
             try {
				deleteFlag = file.delete();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            logger.info("读取文件:{} 报错,直接先删除,删除结果:{}",recentFileName,deleteFlag);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        if (results == null) {
            results = new ArrayList<LotteryIssueResult>();
        } else {
            results.sort(new LotteryIssueResultComparator());
        }
        for(LotteryIssueResult tempResult:results) {
        	if(tempResult.getIssue().equals(issueResult.getIssue())) {
        		return;
        	}
        }
        results.add(issueResult);

        results.sort(new LotteryIssueResultComparator());
        if (results.size() > 5) {
            results.remove(5);
        }
        try {
            String contents = gson.toJson(results);
            FileUtils.write(file, contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDayString(LotteryIssueResult issueResult) {
        String string = issueResult.getTime().split(" ")[0];
        return string;
    }

    private String getDayFileName(DrawConfig drarConfig, String dayString) {
        String path = drarConfig.getFilePath() + "/results/" + dayString + ".json";
        return path;
    }

    private String getRecentFileName(DrawConfig drarConfig) {
        String path = drarConfig.getFilePath() + "/results/recent.json";
        return path;
    }
}
