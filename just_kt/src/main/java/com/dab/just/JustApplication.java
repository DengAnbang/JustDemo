package com.dab.just;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by dab on 2018/1/8 0008 14:25
 */

public class JustApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        JustConfig.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
