package com.changjoo.ohyeah.model;

import java.util.ArrayList;

/**
 * Created by Changjoo on 2017-08-11.
 */

public class Req_Fix {
    private String email;
    private ArrayList<FixModel> Fix_ex;

    public Req_Fix(String email, ArrayList<FixModel> Fix_ex) {
        this.email = email;
        this.Fix_ex = Fix_ex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<FixModel> getFix_ex() {
        return Fix_ex;
    }

    public void setFix_ex(ArrayList<FixModel> Fix_ex) {
        this.Fix_ex = Fix_ex;
    }
}
