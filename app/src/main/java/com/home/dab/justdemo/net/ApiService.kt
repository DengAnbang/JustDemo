package com.home.dab.justdemo.net

import com.dab.just.bean.ResultData
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by dab on 2018/1/8 0008 17:01
 */
interface ApiService{
    @POST("app")
    fun pwdLogin(@Query("phone")phone: String, @Query("password")password: String): Observable<ResultData<JsonObject>>
}