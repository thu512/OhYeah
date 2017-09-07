package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-10.
 */

public class Req_Budget {
    private String email;
    private int budget;
    private int set_date;

    public Req_Budget(String email, int budget, int set_date) {
        this.email = email;
        this.budget = budget;
        this.set_date = set_date;
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
}
