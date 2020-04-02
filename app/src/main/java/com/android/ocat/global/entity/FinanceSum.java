package com.android.ocat.global.entity;

public class FinanceSum {
    private Integer totalIn;
    private Integer totalOut;

    public Integer getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(Integer totalIn) {
        this.totalIn = totalIn;
    }

    public Integer getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(Integer totalOut) {
        this.totalOut = totalOut;
    }

    @Override
    public String toString() {
        return "FinanceSum{" +
                "totalIn=" + totalIn +
                ", totalOut=" + totalOut +
                '}';
    }
}
