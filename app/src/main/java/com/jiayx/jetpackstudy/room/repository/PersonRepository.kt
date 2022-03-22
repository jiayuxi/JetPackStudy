package com.jiayx.jetpackstudy.room.repository

import android.content.Context
import android.content.PeriodicSync
import com.jiayx.jetpackstudy.room.bean.Person
import com.jiayx.jetpackstudy.room.dao.PersonDao
import com.jiayx.jetpackstudy.room.database.PersonDatabase
import kotlinx.coroutines.flow.Flow

/**
 *Created by yuxi_
on 2022/3/8
 */
class PersonRepository(private val context: Context) {
    private val personDao: PersonDao by lazy {
        PersonDatabase.getInstance(context).personDao()
    }

    fun getAllPersons() = personDao.getAllPersons()
    suspend fun deleteAll() {
        personDao.deleteAll()
    }

    suspend fun deleteSelect() {
        personDao.deleteAll(true)
    }

    suspend fun insert(vararg persons: Person) {
        personDao.insertPersons(*persons)
    }

    suspend fun deletePerson(vararg persons: Person) {
        personDao.deletes(*persons)
    }

    suspend fun updateAll(flag: Boolean) {
        personDao.updateAll(flag)
    }

    suspend fun updatePerson(person: Person) {
        personDao.updatePerson(person)
    }

    fun getSelectCount(): Flow<Int> = personDao.getCountSelect()
}