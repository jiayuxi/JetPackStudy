package com.jiayx.jetpackstudy.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jiayx.jetpackstudy.room.bean.StudentBean
import kotlinx.coroutines.flow.Flow

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

    @Query("UPDATE student SET timeLog =:timeLong WHERE id =:id")
    suspend fun updateStudent(timeLong: Long, id: Int)

    @Query("UPDATE student SET timeLog =:timeLong WHERE name =:name")
    suspend fun updateStudent(timeLong: Long, name: String)

    @Delete
    suspend fun deleteStudent(vararg student: StudentBean?)

    @Delete
    suspend fun deleteStudent(student: StudentBean?)

    @Query("DELETE FROM student")
    suspend fun deleteAll()

    @Query("DELETE FROM student WHERE name =:name")
    suspend fun deleteToName(name: String)

    @Query("SELECT * FROM student")
    fun getAllStudent(): LiveData<List<StudentBean>>

    @Query("SELECT * FROM  student")
    fun getFlowStudent(): Flow<List<StudentBean>>
}