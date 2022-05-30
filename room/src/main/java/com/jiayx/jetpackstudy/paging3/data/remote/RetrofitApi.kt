package com.jiayx.jetpackstudy.paging3.data.remote

import android.util.Log
import com.jiayx.jetpackstudy.paging3.model.ArticleData
import com.jiayx.jetpackstudy.paging3.utils.BasePagingResp
import com.jiayx.jetpackstudy.paging3.utils.BaseResp
import com.jiayx.jetpackstudy.paging3.utils.Constants.RETROFIT_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *Created by yuxi_
on 2022/5/30
 */
interface RetrofitApi {

    @GET("article/list/{page}/json")
    suspend fun getHomeArticle(@Path("page") page: Int): BaseResp<BasePagingResp<List<ArticleData>>>


    companion object{
        fun create(): RetrofitApi {
            val logger =
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("jia_okhttp", it) })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(RETROFIT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RetrofitApi::class.java)
        }
    }
}