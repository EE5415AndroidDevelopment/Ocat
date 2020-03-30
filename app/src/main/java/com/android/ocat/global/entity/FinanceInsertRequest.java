package com.android.ocat.global.entity;

import java.util.List;

/**
 * 暂时废弃
 */
public class FinanceInsertRequest {
    private List<FinanceInsertData> data;

    public List<FinanceInsertData> getData() {
        return data;
    }

    public void setData(List<FinanceInsertData> data) {
        this.data = data;
    }
}
