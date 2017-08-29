package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-01.
 */

public class Req_login {
    private String email;
    private String pw;


    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
