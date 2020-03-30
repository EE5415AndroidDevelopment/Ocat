package com.android.ocat.global.entity;

public class FinanceRecord {
    private int id;
    private FinanceCategory cateId;
    private User userId;
    private int inOut;
    private CurrencyCode currencyCode;
    private String currencyValue;
    private String consumeTime;
    private String createTime;
    private String updateTime;

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(String consumeTime) {
        this.consumeTime = consumeTime;
    }

    public FinanceCategory getCateId() {
        return cateId;
    }

    public void setCateId(FinanceCategory cateId) {
        this.cateId = cateId;
    }

    public int getInOut() {
        return inOut;
    }

    public void setInOut(int inOut) {
        this.inOut = inOut;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "FinanceRecord{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", userId=" + userId +
                ", inOut=" + inOut +
                ", currencyCode=" + currencyCode +
                ", currencyValue='" + currencyValue + '\'' +
                ", consumeTime='" + consumeTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
