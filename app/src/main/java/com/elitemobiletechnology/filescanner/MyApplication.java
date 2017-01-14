package com.elitemobiletechnology.filescanner;

import android.app.Application;
import android.content.Context;

/**
 * Created by SteveYang on 17/1/14.
 */

public class MyApplication extends Application {
    private static Context appContext;
    @Override
    public void onCreate(){
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return appContext;
    }
}
