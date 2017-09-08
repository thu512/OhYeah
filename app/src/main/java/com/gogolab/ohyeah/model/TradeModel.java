package com.gogolab.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-06.
 */

public class TradeModel {
    String time;
    String content;
    int money;

    public TradeModel(String time, String content, int money) {
        this.time = time;
        this.content = content;
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
