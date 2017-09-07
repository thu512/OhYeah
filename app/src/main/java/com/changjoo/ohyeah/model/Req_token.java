package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-29.
 */

public class Req_token {
    private String email;
    private String token;

    public Req_token(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
