package com.changjoo.ohyeah.model;

/**
 * Created by Changjoo on 2017-08-09.
 */

public class FixModel {
    String name;
    int money;

    public FixModel(String name, int money) {
        this.name = name;
        this.money = money;
    }

    @Override
    public String toString() {
        return "FixModel{" +
                "name='" + name + '\'' +
                ", money=" + money +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
