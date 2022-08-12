package com.a3.lottery.domain;

import java.io.Serializable;

public class LotteryIssueResult implements  Serializable, Cloneable  {

    /**
     * 
     */
    private static final long serialVersionUID = 11L;
    private int index;
    private String time;
    private String code;
    private String issue;
    private String yuLiu;
    private String opentimestamp;
    private String kenoCode;
    private String pk10Code;
    private String hashValue;
    private String code1; // 防攻击次数
    private String code2;// 异常次数
    private String blockId;// 异常次数
    private String transactionsCount;// 异常次数
    private String blockHash;
    private String shaHash;
    private String val;
    private String tjTime;
    private String huobiCode;
    private String bianCode;
    
    public String getHuobiCode() {
		return huobiCode;
	}

	public void setHuobiCode(String huobiCode) {
		this.huobiCode = huobiCode;
	}

	public String getBianCode() {
		return bianCode;
	}

	public void setBianCode(String bianCode) {
		this.bianCode = bianCode;
	}

	public String getTjTime() {
		return tjTime;
	}

	public void setTjTime(String tjTime) {
		this.tjTime = tjTime;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getShaHash() {
		return shaHash;
	}

	public void setShaHash(String shaHash) {
		this.shaHash = shaHash;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getTransactionsCount() {
		return transactionsCount;
	}

	public void setTransactionsCount(String transactionsCount) {
		this.transactionsCount = transactionsCount;
	}

	public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getPk10Code() {
        return pk10Code;
    }

    public void setPk10Code(String pk10Code) {
        this.pk10Code = pk10Code;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getKenoCode() {
        return kenoCode;
    }

    public void setKenoCode(String kenoCode) {
        this.kenoCode = kenoCode;
    }

    public String getOpentimestamp() {
        return opentimestamp;
    }

    public void setOpentimestamp(String opentimestamp) {
        this.opentimestamp = opentimestamp;
    }

    public String getYuLiu() {
        return yuLiu;
    }

    public void setYuLiu(String yuLiu) {
        this.yuLiu = yuLiu;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return "KenoIssueResult [index=" + index + ", time=" + time + ", code=" + code + ", issue=" + issue + "]";
    }
}
