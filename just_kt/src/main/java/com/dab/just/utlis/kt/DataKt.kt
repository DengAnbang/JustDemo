package com.dab.just.utlis.kt

/**
 * Created by dab on 2018/1/6 0006 15:57
 */
fun Boolean?.isTrue(): Boolean {
    if (this == null) return false
    return this
}
fun Long.toTimeString()=if (this <= 0)  "00:00" else String.format("%02d:%02d",this / 60,this % 60)

fun Int.toTimeString()=if (this <= 0)  "00:00" else String.format("%02d:%02d",this / 60,this % 60)
