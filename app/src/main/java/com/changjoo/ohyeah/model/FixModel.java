package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-09.
 */

public class FixModel {
    String f_ex_record;
    int f_ex_money;

    public FixModel(String f_ex_record, int f_ex_money) {
        this.f_ex_record = f_ex_record;
        this.f_ex_money = f_ex_money;
    }

    @Override
    public String toString() {
        return "FixModel{" +
                "f_ex_record='" + f_ex_record + '\'' +
                ", f_ex_money=" + f_ex_money +
                '}';
    }

    public String getF_ex_record() {
        return f_ex_record;
    }

    public void setF_ex_record(String f_ex_record) {
        this.f_ex_record = f_ex_record;
    }

    public int getF_ex_money() {
        return f_ex_money;
    }

    public void setF_ex_money(int f_ex_money) {
        this.f_ex_money = f_ex_money;
    }
}
