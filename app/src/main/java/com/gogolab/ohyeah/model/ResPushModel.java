package com.gogolab.ohyeah.model;

/**
 * Created by Tacademy on 2017-07-07.
 */

public class ResPushModel {
    private int result;
    private String body;
    private String title;
    private Fix fix;
    private int goal_money;

    public int getGoal_money() {
        return goal_money;
    }

    public void setGoal_money(int goal_money) {
        this.goal_money = goal_money;
    }

    public Fix getFix() {
        return fix;
    }

    public void setFix(Fix fix) {
        this.fix = fix;
    }

    @Override
    public String toString() {
        return "ResPushModel{" +
                "result=" + result +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", fix=" + fix +
                ", goal_money=" + goal_money +
                '}';
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
