package com.dab.just.utlis;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.dab.just.JustConfig;

/**
 * Created by dab on 2018/1/4 0004 18:22
 */

public class ToastUtils {
    public static void showToast(@NonNull Context context, String msg) {
        android.widget.Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String msg) {
        android.widget.Toast.makeText(JustConfig.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
