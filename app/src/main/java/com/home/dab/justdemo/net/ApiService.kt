package com.home.dab.justdemo.net

import com.dab.just.bean.ResultData
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by dab on 2018/1/8 0008 17:01
 */
interface ApiService{
    @FormUrlEncoded
    @POST("app")
    fun pwdLogin(@Field("phone")phone: String, @Field("password")password: String): Observable<ResultData<JsonObject>>
//    @POST("app")
//    fun pwdLogin(@Query("phone")phone: String, @Query("password")password: String): Observable<ResultData<JsonObject>>
}