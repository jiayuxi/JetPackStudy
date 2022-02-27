package com.jiayx.jetpackstudy.room.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jiayx.jetpackstudy.room.bean.StudentBean
import com.jiayx.jetpackstudy.room.dao.StudentDao

/**
 *Created by yuxi_
on 2022/2/27
 */
@Database(entities = [StudentBean::class], version = 2, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun getStudentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null
        fun getInstance(context: Context): StudentDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, StudentDatabase::class.java, "my_db1.db")
                .build()
    }
}