package com.gogolab.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-25.
 */

public class Req_Change_pw {
    private String email;
    private String past_pw;
    private String new_pw;

    public Req_Change_pw(String email, String past_pw, String new_pw) {
        this.email = email;
        this.past_pw = past_pw;
        this.new_pw = new_pw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPast_pw() {
        return past_pw;
    }

    public void setPast_pw(String past_pw) {
        this.past_pw = past_pw;
    }

    public String getNew_pw() {
        return new_pw;
    }

    public void setNew_pw(String new_pw) {
        this.new_pw = new_pw;
    }
}
