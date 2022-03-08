package com.jiayx.jetpackstudy.room.dao

import androidx.paging.DataSource
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jiayx.jetpackstudy.room.bean.Person

/**
 *Created by yuxi_
on 2022/3/1
 */
@Dao
interface PersonDao {
    @Insert
    fun insertPerson(person: Person)

    @Insert
    fun insertPersons(persons: List<Person>)

    @Delete
    fun deletePerson(person: Person)

    @Query("SELECT * FROM Person ORDER BY name COLLATE NOCASE ASC")
    fun getAllPersons(): PagingSource<Int,Person>
}