package com.home.dab.justdemo.net

import com.dab.just.bean.ResultData
import com.dab.just.net.http.JustHttpManager
import com.google.gson.JsonObject
import io.reactivex.Observable

/**
 * Created by dab on 2018/1/8 0008 16:33
 */
object HttpManager : JustHttpManager() {
    /**
     * 密码登录
     */
    fun paswLogin(phone: String, password: String): Observable<ResultData<JsonObject>> {
        return getApiService(ApiService::class.java).pwdLogin(phone, password)
    }
}
