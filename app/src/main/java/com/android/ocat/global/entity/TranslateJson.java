package com.android.ocat.global.entity;

import java.util.List;

public class TranslateJson {
    private String from;
    private String to;
    private List<TranslateResult> trans_result;
    //    private String src;
//    private String dst;
    private int errorCode;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TranslateResult> getTransResult() {
        return trans_result;
    }

    public void setTransResult(List<TranslateResult> trans_result) {
        this.trans_result = trans_result;
    }

    //    public String getSrc() {
//        return src;
//    }
//
//    public void setSrc(String src) {
//        this.src = src;
//    }
//
//    public String getDst() {
//        return dst;
//    }
//
//    public void setDst(String dst) {
//        this.dst = dst;
//    }
//
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "TranslateJson{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", trans_result=" + trans_result +
                ", errorCode=" + errorCode +
                '}';
    }
}
