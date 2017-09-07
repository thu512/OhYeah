package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-18.
 */

public class Req_Purpose {

    private String email;//: 이메일,
    private int goal_money; //: 목표금액,
    private int goal_item; //: 목표아이템   1.집  2.차  3.비행기  4.선물  5.하트  6.로고

    public Req_Purpose(String email, int goal_money, int goal_item) {
        this.email = email;
        this.goal_money = goal_money;
        this.goal_item = goal_item;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGoal_money() {
        return goal_money;
    }

    public void setGoal_money(int goal_money) {
        this.goal_money = goal_money;
    }

    public int getGoal_item() {
        return goal_item;
    }

    public void setGoal_item(int goal_item) {
        this.goal_item = goal_item;
    }
}
