package com.android.ocat.global.entity;

/**
 * 暂时废弃
 */
public class FinanceInsertData {
    private int uid;
    private int cateId;
    private int inOut;
    private int currencyCode;
    private String currencyValue;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getInOut() {
        return inOut;
    }

    public void setInOut(int inOut) {
        this.inOut = inOut;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }

    @Override
    public String toString() {
        return "FinanceInsertData{" +
                "uid=" + uid +
                ", cateId=" + cateId +
                ", inOut=" + inOut +
                ", currencyCode=" + currencyCode +
                ", currencyValue='" + currencyValue + '\'' +
                '}'+"\n";
    }
}
