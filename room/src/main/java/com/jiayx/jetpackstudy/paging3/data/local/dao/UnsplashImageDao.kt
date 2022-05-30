package com.jiayx.jetpackstudy.paging3.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiayx.jetpackstudy.paging3.model.Hits
import com.jiayx.jetpackstudy.paging3.model.ImageBean

@Dao
interface UnsplashImageDao {

    @Query("SELECT * FROM unsplash_image_table")
    fun getAllImages(): PagingSource<Int, ImageBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImages(images: List<ImageBean>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImages(images: ImageBean)

    @Query("DELETE FROM unsplash_image_table")
    suspend fun deleteAllImages()

}