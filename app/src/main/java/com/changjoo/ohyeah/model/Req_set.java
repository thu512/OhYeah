package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-29.
 */


//예산 및 비상금 및 월급일 수정

public class Req_set {
    private String email;
    private int budget;
    private int set_date;
    private int spare_money;

    public Req_set(String email, int budget, int set_date, int spare_money) {
        this.email = email;
        this.budget = budget;
        this.set_date = set_date;
        this.spare_money = spare_money;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getSet_date() {
        return set_date;
    }

    public void setSet_date(int set_date) {
        this.set_date = set_date;
    }

    public int getSpare_money() {
        return spare_money;
    }

    public void setSpare_money(int spare_money) {
        this.spare_money = spare_money;
    }
}
