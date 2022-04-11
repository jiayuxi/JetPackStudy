package com.jiayx.coroutine.retrofit

import com.hencoder.coroutinescamp.model.Repo
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *Created by yuxi_
on 2022/4/11
 */
interface RetrofitApi {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<Repo>>

    @GET("users/{user}/repos")
    suspend fun listReposKt(@Path("user") user: String): List<Repo>

    @GET("users/{user}/repos")
    fun listReposRx(@Path("user") user: String): Single<List<Repo>>

}