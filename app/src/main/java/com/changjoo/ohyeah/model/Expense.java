package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-23.
 */

public class Expense {

    String date;
    String time;
    String record;
    int money;
    String ex_in;
    String today_yn;

    @Override
    public String toString() {
        return "Expense{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", record='" + record + '\'' +
                ", money=" + money +
                ", ex_in='" + ex_in + '\'' +
                ", today_yn='" + today_yn + '\'' +
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
