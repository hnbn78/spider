package com.a3.lottery.module;

import java.util.Comparator;

import com.a3.lottery.domain.LotteryIssueResult;

public class LotteryIssueResultComparator implements Comparator<LotteryIssueResult> {

    public int compare(LotteryIssueResult o1, LotteryIssueResult o2) {
        return -o1.getIssue().compareTo(o2.getIssue());
    }
}