package com.gogolab.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-23.
 */

public class Expense {

    private String date;
    private String time;
    private String record;
    private int money;
    private String ex_in;
    private String today_yn;
    private String month_yn;

    public String getMonth_yn() {
        return month_yn;
    }

    public void setMonth_yn(String month_yn) {
        this.month_yn = month_yn;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", record='" + record + '\'' +
                ", money=" + money +
                ", ex_in='" + ex_in + '\'' +
                ", today_yn='" + today_yn + '\'' +
                ", month_yn='" + month_yn + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getEx_in() {
        return ex_in;
    }

    public void setEx_in(String ex_in) {
        this.ex_in = ex_in;
    }

    public String getToday_yn() {
        return today_yn;
    }

    public void setToday_yn(String today_yn) {
        this.today_yn = today_yn;
    }
}
