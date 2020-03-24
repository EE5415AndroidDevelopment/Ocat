package com.android.ocat.global.entity;

import java.util.List;

public class CurrencyRateResponse {
    private String baseCountry;
    private String baseCurrency;
    private List<Rates> rates;

    public String getBaseCountry() {
        return baseCountry;
    }

    public void setBaseCountry(String baseCountry) {
        this.baseCountry = baseCountry;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public List<Rates> getRates() {
        return rates;
    }

    public void setRates(List<Rates> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "CurrencyRateResponse{" +
                "baseCountry='" + baseCountry + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", rates=" + rates +
                '}';
    }
}
