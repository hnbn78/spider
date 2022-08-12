package com.a3.lottery.spider.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a3.lottery.domain.LotteryIssueResult;
import com.a3.lottery.util.CodeUtil;
import com.a3.lottery.util.SHA256Util;

public class TronRandomCodeTask implements Callable<Map<String, LotteryIssueResult>> {

    private static Logger logger = LoggerFactory.getLogger(TronRandomCodeTask.class);

    private LotteryIssueResult issueVo;
    private Integer count;

    public TronRandomCodeTask(LotteryIssueResult issueVo, Integer count) {
        this.issueVo = issueVo;
        this.count = count;
    }

    @Override
    public Map<String, LotteryIssueResult> call() throws Exception {
        Map<String, LotteryIssueResult> map = new HashMap<String, LotteryIssueResult>();
        while (map.size() < count) {
            String generateHashStr = CodeUtil.generateTronHashStr(issueVo.getBlockHash());
            
            String needSha256Str =generateHashStr+issueVo.getTransactionsCount();
			String sha256StrJava = SHA256Util.getSHA256StrJava(needSha256Str);
			
	        LotteryIssueResult issueResult = new LotteryIssueResult();
	        issueResult.setBlockHash(generateHashStr);
	        issueResult.setShaHash(sha256StrJava);
	        String code2 = SHA256Util.getNumFromSha256Str2(sha256StrJava);
	        issueResult.setCode(code2);
	        
	        if (map.containsKey(code2)) {
                continue;
            }
            map.put(code2, issueResult);
        }
        return map;
    }

}
