package com.dab.just.net.http

/**
 * Created by dab on 2018/1/5 0005 18:30
 */
abstract class JustHttpManager {
    abstract fun getBaseUrl():String

    companion object {
        val PAGE_SIZE = 15//每页的数量
        val DeBugRequest = true//请求的日志
    }
    open fun <T>getApiService(clazz: Class<T>):T {
        return JustRetrofit.getInstance(this).create(clazz)
    }

//    var api:Any?=null
//    open fun <T>getApiService(clazz: Class<T>):T {
//        if (api == null) {
//            api = JustRetrofit.getInstance(this).create(clazz)
//        }
//        return (api as T)
//    }
}