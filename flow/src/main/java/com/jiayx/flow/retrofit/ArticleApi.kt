package com.jiayx.flow.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Created by yuxi_
on 2022/4/1
 */
interface ArticleApi {
    @GET("article")
    suspend fun searchArticles(
        @Query("key") key: String
    ): List<Article>
}