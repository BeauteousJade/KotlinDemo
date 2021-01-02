package com.jade.kotlindemo.page.paging3

import com.jade.kotlindemo.page.paging3.dataBase.Message
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Service {

    @FormUrlEncoded
    @POST("/message/getRandMessage")
    suspend fun getMessage(@Field("count") count: Int): List<Message>

    @FormUrlEncoded
    @POST("/message/getCountMessageFromPosition")
    suspend fun getMessage(
        @Field("count") count: Int,
        @Field("position") position: Int
    ): List<Message>

    companion object {
        private val mRetrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.108:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun create(): Service = mRetrofit.create(Service::class.java)
    }
}