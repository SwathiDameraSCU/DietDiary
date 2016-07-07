package com.mobileapp.finalproject.dietdiary.module;

/**
 * Created by Aasawari on 6/3/15.
 */
public class HistoryDataVO {
    private String dateStr;

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    private int stepCount;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}
