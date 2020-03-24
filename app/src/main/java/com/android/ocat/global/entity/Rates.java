package com.android.ocat.global.entity;

public class Rates {
    private int id;
    private String name;
    private String name_zh;
    private String code;
    private String currency_name;
    private String currency_name_zh;
    private String currency_code;
    private float rate;
    private double hits;
    private double selected;
    private double top;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_zh() {
        return name_zh;
    }

    public void setName_zh(String name_zh) {
        this.name_zh = name_zh;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getCurrency_name_zh() {
        return currency_name_zh;
    }

    public void setCurrency_name_zh(String currency_name_zh) {
        this.currency_name_zh = currency_name_zh;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public double getHits() {
        return hits;
    }

    public void setHits(double hits) {
        this.hits = hits;
    }

    public double getSelected() {
        return selected;
    }

    public void setSelected(double selected) {
        this.selected = selected;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", name_zh='" + name_zh + '\'' +
                ", code='" + code + '\'' +
                ", currency_name='" + currency_name + '\'' +
                ", currency_name_zh='" + currency_name_zh + '\'' +
                ", currency_code='" + currency_code + '\'' +
                ", rate=" + rate +
                ", hits=" + hits +
                ", selected=" + selected +
                ", top=" + top +
                '}'+"\n";
    }
}
