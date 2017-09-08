package com.gogolab.ohyeah.model;

/**
 * Created by Changjoo on 2017-09-02.
 */

public class Req_ModiFix {
    private String email;
    private int fix_yn; // : 고정지출 여부(yes:1, no:0),
    private Fix fix;


    public Req_ModiFix(String email, int fix_yn, Fix fix) {
        this.email = email;
        this.fix_yn = fix_yn;
        this.fix = fix;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFix_yn() {
        return fix_yn;
    }

    public void setFix_yn(int fix_yn) {
        this.fix_yn = fix_yn;
    }

    public Fix getFix() {
        return fix;
    }

    public void setFix(Fix fix) {
        this.fix = fix;
    }


}
