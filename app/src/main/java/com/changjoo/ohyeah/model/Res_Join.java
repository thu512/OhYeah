package com.changjoo.ohyeah.model;

/**
 * Created by Tacademy on 2017-07-14.
 */

public class Res_Join {

    int code;       //응답코드 1:성공, -1:실패
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Res_Join{" +
                "code=" + code +
                ", email='" + email + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    String msg;     //응답 메세지
}
