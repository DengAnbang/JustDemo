package com.dab.just;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.dab.just.utlis.StackManager;

import java.io.File;

/**
 * Created by dab on 2018/1/4 0004 13:33
 */

public class JustConfig {
    private static Context mApplicationContext;
    private static String baseFilePath;
    public static boolean DeBug = true;

    public static String getBaseFilePath() {
        if (TextUtils.isEmpty(baseFilePath)) {
            //有外部存储
            if (getApplicationContext().getExternalCacheDir() == null) {
                baseFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + getApplicationContext().getPackageName() + "/cache/";
            } else {
                baseFilePath = getApplicationContext().getExternalCacheDir().getAbsolutePath() + "/";
            }
            File file = new File(baseFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return baseFilePath;
    }

    public static Context getApplicationContext() {
        if (mApplicationContext == null) {
            throw new NullPointerException("未初始化,请先调用JustConfig.init()方法");
        }
        return mApplicationContext;
    }

    public static void init(Application context) {
        mApplicationContext = context.getApplicationContext();
        StackManager.initStackManager(context);
    }
}
