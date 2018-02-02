package com.dab.just.utlis

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.dab.just.utlis.kt.toTimeString
import java.util.*

/**
 * Created by Wendell on 2018/1/25 11:07
 */

/**
 * 获取设备id
 */
@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)


fun Long.getTimeDDMM(nowTime: Long = System.currentTimeMillis()): String {
    fun getStartTimeOfDay(now: Long): Long {
        val tz = "GMT+8"
        val curTimeZone = TimeZone.getTimeZone(tz)
        val calendar = Calendar.getInstance(curTimeZone)
        calendar.timeInMillis = now
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    var desc = ""
    val startTimeOfDay = getStartTimeOfDay(nowTime)
    val l = this - startTimeOfDay
    val data = l / 1000 / 24 / 60 / 60
    when (data.toInt()) {
        -1 -> desc = "前天"
        0 -> desc = if (l > 0) "今天" else "昨天"
        1 -> desc = "明天"
        2 -> desc = "后天"
    }
    if (desc == "今天" || desc == "明天") {
        desc += this.toTimeString("HH:mm")
    } else {
        desc = this.toTimeString("yyyy年MM月dd日 HH:mm")
    }
    return desc
}
