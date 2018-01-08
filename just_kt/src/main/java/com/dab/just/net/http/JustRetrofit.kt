package com.dab.just.net.http

import com.dab.just.utlis.extend.loge
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 */
class JustRetrofit private constructor() {
companion object {
    val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        JustRetrofit()
    }
}
    private val retrofit by lazy {
        val builder = Retrofit.Builder()
                .baseUrl(JustHttpManager.BASE_URL)//注意此处,设置服务器的地址
                .addConverterFactory(GsonConverterFactory.create())//用于Json数据的转换,非必须
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//用于返回Rxjava调用,非必须
        if (JustHttpManager.DeBugRequest) {
            builder.client(OkHttpClient.Builder()
                    .addNetworkInterceptor { chain ->
                        val request = chain.request()
                        val response = chain.proceed(request)
                        loge(request?.url().toString(),myTag = "request")
                        val peekBody = response.peekBody(1024 * 1024)
                        loge(peekBody.string(),myTag = "response")
                        response!!
                    }.build())
        }
        builder.build()
    }

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

}