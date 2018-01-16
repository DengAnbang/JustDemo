package com.dab.just.net.http

/**
 * Created by dab on 2018/1/5 0005 18:30
 */
open class JustHttpManager {

    companion object {
        val PAGE_SIZE = 15//每页的数量
        val DeBugRequest = true//请求的日志
        val isExtranet = false//是否外网 true 外网
        val SOCKET_SERVER = if (isExtranet) "" else "192.168.3.228"
        val BASE_URL = if (isExtranet) "" else "http://192.168.1.3:9090/"
        val SOCKET_PORT = 8066
        //高德web端url
        val GAODE_URL = "http://restapi.amap.com/v3/"
    }
    var api:Any?=null
    open fun <T>getApiService(clazz: Class<T>):T {
        if (api == null) {
            api = JustRetrofit.instance.create(clazz)
        }
        return (api as T)
    }
   open fun <T>getNewApiService(clazz: Class<T>):T {
        return JustRetrofit.instance.create(clazz)
    }
}