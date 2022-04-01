package com.jiayx.flow.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *Created by yuxi_
on 2022/4/1

单例模式
 */
object RetrofitClient {
    private val instance: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl("http://10.0.0.130:8080/kotlinstudyserver/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val articleApi: ArticleApi by lazy {
        instance.create(ArticleApi::class.java)
    }
}