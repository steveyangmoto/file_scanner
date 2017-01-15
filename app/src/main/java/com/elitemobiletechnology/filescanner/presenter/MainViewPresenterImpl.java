package com.elitemobiletechnology.filescanner.presenter;

import android.app.ActivityManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.elitemobiletechnology.filescanner.Utils;
import com.elitemobiletechnology.filescanner.model.ExtStorageInfo;
import com.elitemobiletechnology.filescanner.view.MainView;

import java.io.File;

/**
 * Created by SteveYang on 17/1/14.
 */

public class MainViewPresenterImpl implements MainViewPresenter {
    private Handler handler = new Handler(Looper.getMainLooper());
    private MainView mainView;
    private FileAnalyzer fileAnalyzer;
    private ExtStorageInfo info;
    private Thread t;

    public MainViewPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void startScan() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                File extDir = Utils.getExtStorageDir();
                if (extDir != null) {
                    fileAnalyzer = new FileAnalyzerImpl(extDir, new FileAnalyzerImpl.FileAnalyzerListener() {
                        @Override
                        public void onProgressUpdate(int progress, String path) {
                            updateProgress(progress,path);
                        }

                        @Override
                        public void onComplete(final ExtStorageInfo info) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mainView.onScanComplete(info);
                                }
                            });
                        }
                    });
                    fileAnalyzer.startAnalysis();
                }
            }
        });
        t.start();
    }

    @Override
    public void stopScan() {
        if(fileAnalyzer!=null){
            fileAnalyzer.stopAnalysis();
        }
    }

    private void updateProgress(final int progress, final String filePath) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainView.onProgressUpdate(progress, filePath);
            }
        });
    }
    @Override
    public void destroy(){
        stopScan();
        handler.removeCallbacksAndMessages(null);
    }

}
