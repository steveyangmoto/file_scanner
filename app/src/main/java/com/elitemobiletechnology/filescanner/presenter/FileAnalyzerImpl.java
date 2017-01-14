package com.elitemobiletechnology.filescanner.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.elitemobiletechnology.filescanner.MyApplication;
import com.elitemobiletechnology.filescanner.R;
import com.elitemobiletechnology.filescanner.Utils;
import com.elitemobiletechnology.filescanner.model.ExtStorageInfo;
import com.elitemobiletechnology.filescanner.model.ExtensionFrequency;
import com.elitemobiletechnology.filescanner.model.FileSizeInfo;
import com.elitemobiletechnology.filescanner.model.MyInt;

import java.io.File;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by SteveYang on 17/1/14.
 */

public class FileAnalyzerImpl implements FileAnalyzer {
    private ArrayList<FileSizeInfo> biggestFiles;
    private HashMap<String, Integer> fileExtFrequencyMap;
    private FileAnalyzerListener listener;
    private File rootDir;
    private int totalFileCount;
    private int totalSize;
    private boolean stopCommanded;

    public interface FileAnalyzerListener {
        void onProgressUpdate(int Progress, String path);

        void onComplete(ExtStorageInfo info);
    }

    public FileAnalyzerImpl(File rootDir, FileAnalyzerListener listener) {
        this.rootDir = rootDir;
        this.listener = listener;
        this.biggestFiles = new ArrayList<>();
        this.fileExtFrequencyMap = new HashMap<>();
    }

    @Override
    public void startAnalysis() {
        stopCommanded = false;
        if (listener != null) {
            listener.onProgressUpdate(1, MyApplication.getAppContext().getString(R.string.analysis_init));
        }
        totalFileCount = countFiles(rootDir);
        if (listener != null) {
            if(stopCommanded){
                listener.onComplete(null);
                return;
            }
            if (analyzeFile(rootDir, new MyInt(0))) {
                if(stopCommanded){
                    listener.onComplete(null);
                }else {
                    ExtStorageInfo info = new ExtStorageInfo();
                    info.setBiggestFiles(this.biggestFiles);
                    info.setAvgFileSize(totalSize / totalFileCount);
                    info.setMostFrequentFileExts(getMostFrequentExts());
                    listener.onComplete(info);
                }
            } else {
                listener.onComplete(null);
            }
        }
    }

    @Override
    public void stopAnalysis() {
        stopCommanded = true;
    }

    private ArrayList<ExtensionFrequency> getMostFrequentExts() {
        ArrayList<ExtensionFrequency> mostFrequentExts = new ArrayList<>();
        if (fileExtFrequencyMap != null) {
           Iterator iterator = fileExtFrequencyMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String extName = (String) entry.getKey();
                int frequency = (int) entry.getValue();
                ExtensionFrequency extensionFrequency = new ExtensionFrequency(extName, frequency);
                if (mostFrequentExts.size() == 0) {
                    mostFrequentExts.add(extensionFrequency);
                } else {
                    for (int i = 0; i < mostFrequentExts.size() && i < 5; i++) {
                        if (frequency > mostFrequentExts.get(i).getFrequency()) {
                            mostFrequentExts.add(i, extensionFrequency);
                            break;
                        }
                    }
                }
            }
        }
        return mostFrequentExts;
    }

    private boolean analyzeFile(File file, MyInt count) {
        if (file == null||stopCommanded) {
            return false;
        }

        if (file.isFile()) {
            count.inc();
            String name = file.getName();
            long size = file.length();
            FileSizeInfo myFileSizeInfo = new FileSizeInfo(name, size);
            totalSize += size;
            String ext = Utils.getFileType(file.getAbsolutePath());
            if (!TextUtils.isEmpty(ext)) {
                Integer frequency = fileExtFrequencyMap.get(ext);
                int fileExtFrequency = frequency == null ? 1 : frequency+1;
                fileExtFrequencyMap.put(ext, fileExtFrequency);
            }
            int progress = (int) (count.get() * 1.0 / totalFileCount * 100);
            if (biggestFiles.size() > 0) {
                for (int i = 0; i < biggestFiles.size() ; i++) {
                    if(i<10) {
                        FileSizeInfo fileInfo = biggestFiles.get(i);
                        if (size > fileInfo.getSize()) {
                            biggestFiles.add(i, myFileSizeInfo);
                            break;
                        }
                    }else{
                        biggestFiles.remove(i);
                    }
                }
            } else {
                biggestFiles.add(myFileSizeInfo);
            }
            if (listener != null) {
                listener.onProgressUpdate(progress<1?1:progress, file.getAbsolutePath());
            }
        } else if (file.isDirectory()) {
            File[] dirFiles = file.listFiles();
            if(dirFiles!=null) {
                for (File dirFile : dirFiles) {
                    analyzeFile(dirFile, count);
                }
            }
        }
        return true;
    }

    private int countFiles(File rootDir) {
        if(stopCommanded){
            return 0;
        }
        int count = 0;
        if(rootDir!=null) {
            File[] files = rootDir.listFiles();
            if(files!=null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        count += countFiles(file);
                    } else {
                        count++;
                    }
                }
            }
        }
        return count;
    }

}
