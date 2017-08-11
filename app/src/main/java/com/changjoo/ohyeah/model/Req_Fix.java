package com.changjoo.ohyeah.model;

import java.util.ArrayList;

/**
 * Created by Changjoo on 2017-08-11.
 */

public class Req_Fix {
    String email;
    ArrayList<FixModel> fix_ex;

    public Req_Fix(String email, ArrayList<FixModel> fix_ex) {
        this.email = email;
        this.fix_ex = fix_ex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<FixModel> getFix_ex() {
        return fix_ex;
    }

    public void setFix_ex(ArrayList<FixModel> fix_ex) {
        this.fix_ex = fix_ex;
    }
}
