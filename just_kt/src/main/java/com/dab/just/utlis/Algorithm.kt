package com.dab.just.utlis

import java.lang.Exception
import java.security.MessageDigest

/**
 * Created by Wendell on 2018/1/25 11:25
 * 算法
 */
/**
 * hex true 16位的(默认) false 32位
 */
fun md5(str: String,hex:Boolean =true): String {
    try {
        val instance: MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
        val digest:ByteArray = instance.digest(str.toByteArray())//对字符串加密，返回字节数组
        val sb = StringBuffer()
        for (b in digest) {
            val i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0" + hexString//如果是一位的话，补0
            }
            sb.append(hexString)
        }
        val toString = sb.toString()
        return if (hex) toString.substring(8, 24) else toString
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}