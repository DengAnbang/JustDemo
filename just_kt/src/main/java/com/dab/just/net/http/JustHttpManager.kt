package com.dab.just.net.http

/**
 * Created by dab on 2018/1/5 0005 18:30
 */
abstract class JustHttpManager {

    companion object {
        private val isExtranet=true
        val PAGE_SIZE = 15//每页的数量
        val DeBugRequest = true//请求的日志
        val BASE_URL=if (isExtranet) "http://106.15.192.250:8030/api/" else "http://192.168.3.183:8080/api/"
        val PREFIX_PATH=if (isExtranet) "http://106.15.192.250:8030/api" else "http://192.168.3.183:8080/api/"
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