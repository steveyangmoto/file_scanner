package com.elitemobiletechnology.filescanner;

import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by SteveYang on 17/1/14.
 */

public class Utils {
    public static File getExtStorageDir(){
        File storageDir = null;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) && (!Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
            storageDir = Environment.getExternalStorageDirectory();
        }
        return storageDir;
    }

    public static String getFileType(String filePath)
    {
        String fileExtension = null;
        int dotIndex = filePath.lastIndexOf('.');
        if(dotIndex>=0){
            try {
                fileExtension = filePath.substring(dotIndex + 1, filePath.length());
            }catch (Exception ignore){
            }
        }
        return fileExtension;
    }

}
