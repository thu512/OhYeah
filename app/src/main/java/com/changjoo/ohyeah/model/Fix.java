package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-09-02.
 */

public class Fix {
    private String msg_content;
    private int msg_money;
    private String msg_date;
    private String msg_time;
    private String fix_data;

    public Fix(String msg_content, int msg_money, String msg_date, String msg_time, String fix_data) {
        this.msg_content = msg_content;
        this.msg_money = msg_money;
        this.msg_date = msg_date;
        this.msg_time = msg_time;
        this.fix_data = fix_data;
    }

    @Override
    public String toString() {
        return "Fix{" +
                "msg_content='" + msg_content + '\'' +
                ", msg_money=" + msg_money +
                ", msg_date='" + msg_date + '\'' +
                ", msg_time='" + msg_time + '\'' +
                ", fix_data='" + fix_data + '\'' +
                '}';
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public int getMsg_money() {
        return msg_money;
    }

    public void setMsg_money(int msg_money) {
        this.msg_money = msg_money;
    }

    public String getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(String msg_date) {
        this.msg_date = msg_date;
    }

    public String getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(String msg_time) {
        this.msg_time = msg_time;
    }

    public String getFix_data() {
        return fix_data;
    }

    public void setFix_data(String fix_data) {
        this.fix_data = fix_data;
    }
}
