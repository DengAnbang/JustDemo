package com.dab.just.utlis

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

/**
 * Created by Wendell on 2018/1/25 11:07
 */

/**
 * 获取设备id
 */
@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
