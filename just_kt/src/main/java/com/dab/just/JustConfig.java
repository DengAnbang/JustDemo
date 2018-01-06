package com.dab.just;

import android.content.Context;

/**
 * Created by dab on 2018/1/4 0004 13:33
 */

public class JustConfig {
    private static Context mApplicationContext;
    public static boolean DeBug = true;

    public static Context getApplicationContext() {
        if (mApplicationContext == null) {
            throw new NullPointerException("未初始化,请先调用JustConfig.init()方法");
        }
        return mApplicationContext;
    }

    public static void init(Context context) {
        mApplicationContext = context.getApplicationContext();
    }
}
