package com.a3.lottery.spider.bean;

import java.util.Date;

public class LotteryBean {

    private int lotteryId;
    private String lotteryCode;

    public Date closeStart;
    public Date closeEnd;

    public int getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(int lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public Date getCloseStart() {
        return closeStart;
    }

    public void setCloseStart(Date closeStart) {
        this.closeStart = closeStart;
    }

    public Date getCloseEnd() {
        return closeEnd;
    }

    public void setCloseEnd(Date closeEnd) {
        this.closeEnd = closeEnd;
    }

    @Override
    public String toString() {
        return "LotteryBean [lotteryId=" + lotteryId + ", lotteryCode=" + lotteryCode + ", closeStart=" + closeStart
                + ", closeEnd=" + closeEnd + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lotteryCode == null) ? 0 : lotteryCode.hashCode());
        result = prime * result + lotteryId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LotteryBean other = (LotteryBean) obj;
        if (lotteryCode == null) {
            if (other.lotteryCode != null)
                return false;
        } else if (!lotteryCode.equals(other.lotteryCode))
            return false;
        if (lotteryId != other.lotteryId)
            return false;
        return true;
    }

}
