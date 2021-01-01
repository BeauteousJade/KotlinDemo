package com.jade.kotlindemo.page.paging3

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Service {

    @FormUrlEncoded
    @POST("/message/getRandMessage")
    suspend fun getMessage(@Field("count") count: Int): List<Message>

    companion object {
        private val mRetrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.108:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun create() = mRetrofit.create(Service::class.java)
    }
}