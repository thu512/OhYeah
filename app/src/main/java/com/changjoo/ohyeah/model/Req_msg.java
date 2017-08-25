package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-24.
 */

public class Req_msg {
    String email;
    String msg;

    public Req_msg(String email, String msg) {
        this.email = email;
        this.msg = msg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
