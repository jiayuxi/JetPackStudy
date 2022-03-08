package com.jiayx.jetpackstudy.room.repository

import android.content.Context
import com.jiayx.jetpackstudy.room.dao.PersonDao
import com.jiayx.jetpackstudy.room.database.PersonDatabase

/**
 *Created by yuxi_
on 2022/3/8
 */
class PersonRepository(private val context: Context) {
    private val personDao: PersonDao by lazy {
        PersonDatabase.getInstance(context).personDao()
    }

    fun getAllPersons() = personDao.getAllPersons()

}