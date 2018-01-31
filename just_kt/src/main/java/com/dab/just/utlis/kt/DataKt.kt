package com.dab.just.utlis.kt

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dab on 2018/1/6 0006 15:57
 */
fun Boolean?.isTrue(): Boolean {
    if (this == null) return false
    return this
}
/** true为1 */
fun Boolean?.parseInt()=if (this == null || this) 0 else 1
fun Boolean?.parseString()=if (this == null || !this) "0" else "1"
/**
 * int解析成性别1为男
 */
fun Int?.parseSex()=if (this == null || this==1) "男" else "女"

/**
 * 如果为空,则显示默认
 */
fun String?.defaultString(default: String)=if (this == null || this == "") default else this
/**
 * 解析string为list
 */
fun String?.parseStringList():ArrayList<String> {
    this.isNullOrBlank()
    if (this==null)return arrayListOf()
    val split = this.split(",")
    if (split.isEmpty()) return arrayListOf()
    return split as ArrayList<String>
}
fun Long?.toTimeString(pattern: String): String {
    if (this==null)return "00:00"
    val f = SimpleDateFormat(pattern, Locale.CHINA)
    return f.format(Date(this))
}
fun Long.toTimeString()=if (this <= 0)  "00:00" else String.format("%02d:%02d",this / 60,this % 60)

fun Int.toTimeString()=if (this <= 0)  "00:00" else String.format("%02d:%02d", this / 60, this % 60)

/**
 * 解析JsonArray
 * 返回一个arraylist
 */
inline fun <reified T>JsonArray.parse(gson: Gson=Gson())=this.mapTo(ArrayList()) { gson.fromJson<T>(it, T::class.java) }
fun JsonObject?.getString(type:String,default:String="")=this?.get(type)?.asString?:default
fun JsonObject?.getInt(type:String,default:Int=0)=this?.get(type)?.asInt?:default

