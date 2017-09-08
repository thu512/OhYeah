package com.gogolab.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-18.
 */

public class Req_Nest {

    private String email;
    private int spare_money;

    public Req_Nest(String email, int spare_money) {
        this.email = email;
        this.spare_money = spare_money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSpare_money() {
        return spare_money;
    }

    public void setSpare_money(int spare_money) {
        this.spare_money = spare_money;
    }
}
