package com.a3.lottery.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author bean
 *
 */
public class LotteryDrawRequestBean {

    private static final SimpleDateFormat SimpleParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private java.lang.Integer id;
    /**
     * 彩种代码
     */
    private java.lang.String lotteryCode;
    /**
     * 开奖时间戳
     */
    private Long lotteryTime;

    private String lotteryNumber;

    private String issueNo;

    private Integer lotteryId;

    private boolean isSystemOpen;

    private Date openDate;

    private boolean settleResult;

    private String opuser;
    private String yuliu;// 预留字段

    public String getYuliu() {
        return yuliu;
    }

    public void setYuliu(String yuliu) {
        this.yuliu = yuliu;
    }

    public boolean isSettleResult() {
        return settleResult;
    }

    public void setSettleResult(boolean settleResult) {
        this.settleResult = settleResult;
    }

    public String getOpuser() {
        return opuser;
    }

    public void setOpuser(String opuser) {
        this.opuser = opuser;
    }

    public boolean isSystemOpen() {
        return isSystemOpen;
    }

    public void setSystemOpen(boolean isSystemOpen) {
        this.isSystemOpen = isSystemOpen;
    }

    public Integer getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Integer lotteryId) {
        this.lotteryId = lotteryId;
    }

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public Long getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Long lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getLotteryNumber() {
        return lotteryNumber;
    }

    public void setLotteryNumber(String lotteryNumber) {
        this.lotteryNumber = lotteryNumber;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public java.lang.String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(java.lang.String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    @Override
    public String toString() {
        return "LotteryDrawRequestBean [id=" + id + ", lotteryCode=" + lotteryCode + ", lotteryTime=" + lotteryTime
                + ", lotteryNumber=" + lotteryNumber + ", issueNo=" + issueNo + ", lotteryId=" + lotteryId
                + ", isSystemOpen=" + isSystemOpen + ", openDate=" + openDate + ", settleResult=" + settleResult
                + ", opuser=" + opuser + "]";
    }

    public static LotteryDrawRequestBean convert(DrawResult drawResult) {
        LotteryDrawRequestBean bean = new LotteryDrawRequestBean();
        bean.setLotteryCode(drawResult.getLottery());
        bean.setIssueNo(drawResult.getIssue());
        bean.setLotteryNumber(drawResult.getCode());
        String timeStr = drawResult.getTime();

        Long k = null;
        if (StringUtils.isNotBlank(timeStr)) {
            try {
                Date time = SimpleParse.parse(timeStr);
                k = time.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (k == null) {
            k = Long.valueOf(drawResult.getTimestamp()) * 1000;
            if (k < 0) {
                k = new Date().getTime();
            }
        }
        bean.setLotteryTime(k);
        bean.setYuliu(drawResult.getYuliu());
        return bean;
    }

}
