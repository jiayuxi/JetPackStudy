package com.jiayx.jetpackstudy.room.dao

import androidx.paging.DataSource
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.*
import com.jiayx.jetpackstudy.room.bean.Person

/**
 *Created by yuxi_
on 2022/3/1
 */
@Dao
interface PersonDao {
    @Insert
    suspend fun insertPerson(person: Person)

    @Insert
    suspend fun insertPersons(vararg person: Person)

    @Delete
    suspend fun deletePerson(person: Person)

    @Delete
    suspend fun deletes(vararg person: Person)

    @Query("DELETE FROM person")
    suspend fun deleteAll()

    @Query("DELETE FROM person WHERE isSelect =:flag")
    suspend fun deleteAll(flag: Boolean)

    @Update
    suspend fun updatePerson(vararg person: Person)

    @Query("UPDATE person SET isSelect =:flag")
    suspend fun updateAll(flag: Boolean)

    @Query("SELECT * FROM person ORDER BY name COLLATE NOCASE ASC")
    fun getAllPersons(): PagingSource<Int, Person>
}