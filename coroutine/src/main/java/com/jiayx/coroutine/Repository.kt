package com.jiayx.coroutine

import com.hencoder.coroutinescamp.model.Repo
import com.jiayx.coroutine.retrofit.RetrofitApi
import com.jiayx.coroutine.tools.RetrofitTools
import io.reactivex.rxjava3.core.Single
import retrofit2.Call

/**
 *Created by yuxi_
on 2022/4/11
网络请求 操作类
 */
class Repository {
    private val retrofitApi by lazy {
        RetrofitTools.retrofit.create(RetrofitApi::class.java)
    }

    /**
     * 协程 + retrofit 网络请求
     */
    suspend fun loadUsersKt(string: String): List<Repo> = retrofitApi.listReposKt(string)

    /**
     * retrofit 网络请求
     */
    fun retrofit(string: String): Call<List<Repo>> = retrofitApi.listRepos(string)

    /**
     * rxjava + retrofit 网络请求
     */
    fun rxjavaRetrofit(string: String): Single<List<Repo>> = retrofitApi.listReposRx(string)
}