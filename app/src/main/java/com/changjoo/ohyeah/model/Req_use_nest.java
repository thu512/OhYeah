package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-29.
 */

public class Req_use_nest {
    String email;
    int use_spare;

    public Req_use_nest(String email, int use_spare) {
        this.email = email;
        this.use_spare = use_spare;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUse_spare() {
        return use_spare;
    }

    public void setUse_spare(int use_spare) {
        this.use_spare = use_spare;
    }
}
