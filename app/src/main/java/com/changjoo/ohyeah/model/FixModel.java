package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-09.
 */

public class FixModel {
    String name;
    String money;

    public FixModel(String name, String money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
