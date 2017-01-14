package com.elitemobiletechnology.filescanner.model;

/**
 * Created by SteveYang on 17/1/14.
 */


public class MyInt {
    private int number;

    public MyInt(int initialVal) {
        number = initialVal;
    }

    public int get() {
        return number;
    }

    public void inc() {
        number++;
    }
}