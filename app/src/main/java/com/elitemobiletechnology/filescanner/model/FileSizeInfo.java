package com.elitemobiletechnology.filescanner.model;

/**
 * Created by SteveYang on 17/1/14.
 */

public class FileSizeInfo {
    private String name;
    private long size;

    public FileSizeInfo(String name,long size){
        this.name = name;
        this.size = size;
    }

    public String getName(){
        return name;
    }

    public long getSize(){
        return size;
    }
}
