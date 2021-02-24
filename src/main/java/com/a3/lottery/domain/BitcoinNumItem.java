package com.a3.lottery.domain;

public class BitcoinNumItem {

    private int ver;
    private Long time;
    private Long tx_index;
    private int vin_sz;
    private String hash;
    private int vout_sz;
    private Long lock_time;
    private int size;
    private String double_spend;

    public Long getLock_time() {
        return lock_time;
    }

    public void setLock_time(Long lock_time) {
        this.lock_time = lock_time;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDouble_spend() {
        return double_spend;
    }

    public void setDouble_spend(String double_spend) {
        this.double_spend = double_spend;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTx_index() {
        return tx_index;
    }

    public void setTx_index(Long tx_index) {
        this.tx_index = tx_index;
    }

    public int getVin_sz() {
        return vin_sz;
    }

    public void setVin_sz(int vin_sz) {
        this.vin_sz = vin_sz;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getVout_sz() {
        return vout_sz;
    }

    public void setVout_sz(int vout_sz) {
        this.vout_sz = vout_sz;
    }

}
