package com.dab.just.base;

import android.app.Application;
import android.content.Context;

import com.dab.just.JustConfig;

/**
 * Created by dab on 2018/1/8 0008 14:25
 */

public class JustApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        JustConfig.init(this);
    }
}
