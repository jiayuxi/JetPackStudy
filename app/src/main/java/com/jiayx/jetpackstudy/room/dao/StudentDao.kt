package com.jiayx.jetpackstudy.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jiayx.jetpackstudy.room.bean.StudentBean

/**
 *Created by yuxi_
on 2022/2/27
 */
@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(vararg student: StudentBean?)

    @Insert
    suspend fun insertStudent(students: List<StudentBean>?)

    @Insert
    suspend fun insertStudent(student: StudentBean?)

    @Update
    suspend fun updateStudent(vararg student: StudentBean?)

    @Update
    suspend fun updateStudent(student: StudentBean?)

    @Delete
    suspend fun deleteStudent(vararg student: StudentBean?)

    @Delete
    suspend fun deleteStudent(student: StudentBean?)

    @Query("DELETE FROM student")
    suspend fun deleteAll()

    @Query("SELECT * FROM student")
    fun getAllStudent(): LiveData<List<StudentBean>>
}