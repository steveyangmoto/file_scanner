package com.elitemobiletechnology.filescanner.model;

/**
 * Created by SteveYang on 17/1/14.
 */

public class ExtensionFrequency {
    private String name;
    private int frequency;

    public ExtensionFrequency(String name,int frequency){
        this.name = name;
        this.frequency = frequency;
    }

    public String getExtensionName(){
        return name;
    }

    public int getFrequency(){
        return frequency;
    }
}
