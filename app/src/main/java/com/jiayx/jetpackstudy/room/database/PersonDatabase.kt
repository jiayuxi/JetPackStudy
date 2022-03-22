package com.jiayx.jetpackstudy.room.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jiayx.jetpackstudy.room.bean.Person
import com.jiayx.jetpackstudy.room.dao.PersonDao
import java.util.concurrent.Executors
import kotlin.math.log

/**
 *Created by yuxi_
on 2022/3/1
 */
@Database(entities = [Person::class], version = 5, exportSchema = false)
abstract class PersonDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao

    companion object {
        @Volatile
        private var INSTANCE: PersonDatabase? = null
        fun getInstance(context: Context): PersonDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PersonDatabase::class.java,
                "PersonDatabase.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}