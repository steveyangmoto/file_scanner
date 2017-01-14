package com.elitemobiletechnology.filescanner.model;

import java.util.ArrayList;

/**
 * Created by SteveYang on 17/1/14.
 */

public class ExtStorageInfo {
    private ArrayList<FileSizeInfo> biggestFiles;
    private ArrayList<ExtensionFrequency> mostFrequentFileExts;

    private int avgFileSize;

    public ExtStorageInfo(){
        this.biggestFiles = new ArrayList<>();
        this.mostFrequentFileExts = new ArrayList<>();
    }

    public void setBiggestFiles(ArrayList<FileSizeInfo> biggestFiles) {
        this.biggestFiles = biggestFiles;
    }

    public void setAvgFileSize(int avgFileSize) {
        this.avgFileSize = avgFileSize;
    }

    public void setMostFrequentFileExts(ArrayList<ExtensionFrequency> mostFrequentFileExts) {
        this.mostFrequentFileExts = mostFrequentFileExts;
    }

    public int getAvgFileSize() {
        return avgFileSize;
    }

    public ArrayList<FileSizeInfo> getBiggestFiles() {
        return biggestFiles;
    }

    public ArrayList<ExtensionFrequency> getMostFrequentFileExts() {
        return mostFrequentFileExts;
    }

}
