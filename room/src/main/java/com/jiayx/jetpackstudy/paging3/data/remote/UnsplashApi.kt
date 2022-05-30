package com.jiayx.jetpackstudy.paging3.data.remote

import com.jiayx.jetpackstudy.paging3.model.Pixabay
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Created by yuxi_
on 2022/5/24
 */
interface UnsplashApi {
    @GET("?key=12472743-874dc01dadd26dc44e0801d61")
    suspend fun loadImages(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Pixabay
}