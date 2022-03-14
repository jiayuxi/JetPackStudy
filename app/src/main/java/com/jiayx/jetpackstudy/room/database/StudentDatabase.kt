package com.jiayx.jetpackstudy.room.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jiayx.jetpackstudy.room.bean.StudentBean
import com.jiayx.jetpackstudy.room.dao.StudentDao

/**
 *Created by yuxi_
on 2022/2/27
 */
@Database(entities = [StudentBean::class], version = 3, exportSchema = false)
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
            Room.databaseBuilder(
                context.applicationContext,
                StudentDatabase::class.java,
                "my_db1.db"
            )
                .fallbackToDestructiveMigration()
                .addMigrations(migration_2_3)
                .build()

        private val migration_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.e("database_log", "migrate: ${"alter table student add column timeLog INTEGER not null default 0"}")
                database.execSQL("alter table student add column timeLog INTEGER not null default 0")
            }
        }
    }
}