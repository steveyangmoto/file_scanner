package com.elitemobiletechnology.filescanner.view;

import com.elitemobiletechnology.filescanner.model.ExtStorageInfo;

/**
 * Created by SteveYang on 17/1/14.
 */

public interface MainView {
    void onProgressUpdate(int progress,String filePath);
    void onScanComplete(ExtStorageInfo info);
}
