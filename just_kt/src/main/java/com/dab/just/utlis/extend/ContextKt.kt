package com.dab.just.utlis.extend

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

/**
 * Created by dab on 2017/10/20 0020 11:35
 */
fun Context.dp2px(dpValue: Int): Int {
    val scale = this.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.sp2px(dpValue: Int): Int {
    val fontScale = this.resources.displayMetrics.scaledDensity
    return (dpValue * fontScale + 0.5f).toInt()
}

fun Context.getColorKt(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

